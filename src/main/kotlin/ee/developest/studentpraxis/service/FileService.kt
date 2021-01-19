package ee.developest.studentpraxis.service

import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import ee.developest.studentpraxis.exception.FileSizeException
import ee.developest.studentpraxis.exception.UnsupportedFileTypeException
import ee.developest.studentpraxis.model.Advertisement
import ee.developest.studentpraxis.model.CurriculumVitae
import ee.developest.studentpraxis.model.Deal
import ee.developest.studentpraxis.model.InternshipProvider
import ee.developest.studentpraxis.repository.AdvertisementRepository
import ee.developest.studentpraxis.repository.CurriculumVitaeRepository
import ee.developest.studentpraxis.repository.InternshipProviderRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


@Service
class FileService(
        private val userService: UserService,
        private val curriculumVitaeRepository: CurriculumVitaeRepository,
        private val internshipProviderRepository: InternshipProviderRepository,
        private val advertisementRepository: AdvertisementRepository,
        private val dealService: DealService,
        private val companyContactService: CompanyContactService,
        private val contactTypeService: ContactTypeService
) {
    companion object {
        const val MAX_IMG_SIZE = 1000 * 1000 * 25L // 25 MB
    }

    val internProfilePictureDirectory = "profile_picture"
    val companyPictureDirectory = "company_picture"
    val advertisementPictureDirectory = "advertisement_image"
    val cvDirectory = "cv"
    val invoicePdfDirectory = "invoice_pdf"
    val imagePath = "./files/static/studentpraxis_logo.png"

    // ================================================ Intern ================================================

    fun getLastInternProfilePicture(userId: Long): String? {
        return userService.findUserById(userId).image
    }

    @Throws(UnsupportedFileTypeException::class, FileSizeException::class)
    fun saveInternProfilePicture(userId: Long, img: MultipartFile): String? {
        val user = userService.findUserById(userId)
        user.image = saveImage(userId, img, internProfilePictureDirectory)
        return userService.saveUser(user).image
    }

    // ================================================ InternshipProvider ================================================


    @Throws(UnsupportedFileTypeException::class, FileSizeException::class)
    fun saveInternshipProviderPicture(internshipProviderId: Long, img: MultipartFile): String {
        return saveImage(internshipProviderId, img, companyPictureDirectory)
    }

    // ================================================ Advertisement ================================================

    fun getLastAdvertisementPicture(advertisementId: Long): String? {
        val ad = advertisementRepository.findById(advertisementId).orElse(null)
                ?: throw Exception("Advertisement not found")
        return ad.image
    }

    @Throws(UnsupportedFileTypeException::class, FileSizeException::class)
    fun saveAdvertisementPicture(advertisement: Advertisement, img: MultipartFile): String {
        return saveImage(advertisement.id, img, advertisementPictureDirectory)
    }

    // ================================================ CV ================================================

    // TODO Voiks ikkagi cv servicei teha, et siin repository ei näpiks

    fun saveCV(userId: Long, file: MultipartFile): String? {
        // TODO validate cv file type <- isn't it generated?
        val user = userService.findUserById(userId)
        val date = Date()
        val pathString = "/files/$cvDirectory/$userId/"
        val filename = date.time.toString() + "." + (file.originalFilename?.split(".")?.last() ?: return null)

        File(pathString).mkdirs()
        Files.write(Paths.get(pathString + filename), file.bytes)

        // Kui siin voiks uuesti Date() votta, siis saaks saveFile funktsiooni siin ka kasutada.
        // date ja nimi siis ei klapi, aga kood oleks palju ilusam.
        return curriculumVitaeRepository.save(CurriculumVitae(user = user, uploadTime = date, file = pathString + filename)).file
    }

    fun getAllCvsByUserId(userId: Long): List<CurriculumVitae> {
        return curriculumVitaeRepository.findAllByUserId(userId)
    }

    fun getLastCvByUserId(userId: Long): String? {
        val cv = curriculumVitaeRepository.findFirstByUserIdOrderByUploadTimeDesc(userId)
        return cv.orElse(null)?.file
    }

    fun getCvById(id: Long): Optional<CurriculumVitae> {
        // TODO maybe should throw error if not found, if found then return CurriculumVitae instead of Optional<CurriculumVitae>
        return curriculumVitaeRepository.findById(id)
    }

    // ================================================ Pdf ================================================

    fun generatePdfForInvoice(invoiceId: Long, companyId: Long, dealId: Long): String {
        val deal: Deal = dealService.findById(dealId)
        val ip = internshipProviderRepository.findById(companyId).orElse(null) ?: throw Exception("Company not found")
        val document = Document()
        val out = ByteArrayOutputStream()

        try {
            PdfWriter.getInstance(document, out)
            document.open()

            val image = Image.getInstance(imagePath)
            image.scaleAbsolute(125f, 125f)
            image.alignment = Element.ALIGN_CENTER
            document.add(image)

            document.add(Chunk.NEWLINE)

            document.add(getInvoiceHeader(invoiceId, ip))

            document.add(Paragraph(" "))
            document.add(Chunk.NEWLINE)
            document.add(Chunk.NEWLINE)


            document.add(Paragraph(
                    "Klienditellimus",
                    FontFactory.getFont(FontFactory.HELVETICA, 14f, BaseColor.BLACK)))

            document.add(Paragraph(" ", FontFactory.getFont(FontFactory.HELVETICA, 2f)))
            document.add(getInvoiceBody(deal))

            document.add(Paragraph(" "))
            for (i in 0..15) {
                document.add(Chunk.NEWLINE)
            }

            document.add(getBodyFooter(deal))
            document.add(Paragraph(".".repeat(156)))
            document.add(getInvoiceFooter())

            document.close();
        } catch (e: DocumentException) {
            e.printStackTrace()
        }

        val pathString = "/files/$invoicePdfDirectory/$companyId/"
        val filename = Date().time.toString() + ".pdf"

        File(pathString).mkdirs()
        Files.write(Paths.get(pathString + filename), out.toByteArray())
        // TODO Send to email!!
        return pathString + filename
    }

    // ================================================ General ================================================

    @Throws(UnsupportedFileTypeException::class, FileSizeException::class)
    fun saveImage(id: Long, img: MultipartFile, directory: String): String {
        val pathString = "/files/$directory/$id/"
        val filename = Date().time.toString() + "." + (img.originalFilename?.split(".")?.last()
                ?: throw UnsupportedFileTypeException("Unsupported image type!, use .png or .jpg"))

        if (!(filename.endsWith(".png", ignoreCase = true) || filename.endsWith(".jpg", ignoreCase = true)))
            throw UnsupportedFileTypeException("Unsupported image type!, use .png or .jpg")

        if (img.size > MAX_IMG_SIZE)
            throw FileSizeException("Max allowed file size is 25 MB")

        File(pathString).mkdirs()
        Files.write(Paths.get(pathString + filename), img.bytes)

        return pathString + filename
    }

    fun getInvoiceHeader(invoiceId: Long, ip: InternshipProvider): PdfPTable {
        val pattern = "dd.MM.yyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date = simpleDateFormat.format(Date())
        val address = companyContactService.getByCompanyIdAndType(ip.id, contactTypeService.getAddress()).get()

        val table = PdfPTable(5)
        table.widthPercentage = 100.0f;
        // =========================== header =====================================
        val firstRow = ArrayList<String>()
        firstRow.add("Maksja:")
        firstRow.add(ip.companyName)
        firstRow.add(" ")
        firstRow.add("ARVE")
        firstRow.add(invoiceId.toString())


        firstRow.forEach { headerTile ->
            val header = PdfPCell()
            val headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12f, BaseColor.BLACK)
            header.horizontalAlignment = Element.ALIGN_LEFT
            header.borderWidth = 0f
            header.phrase = Phrase(headerTile, headFont)
            table.addCell(header)
        }

        val data = ArrayList<String>()
        data.add(" ")
        data.add(address.value)
        data.add(" ")
        data.add("Kuupäev:")
        data.add(date.toString())

        data.add(" ")
        data.add(" ")
        data.add(" ")
        data.add("Tasumistingimus:")
        data.add("Ettemaksuarve")

        return formatTable(data, table)
    }

    fun getInvoiceBody(deal: Deal): PdfPTable {
        val table = PdfPTable(5)
        table.widthPercentage = 100.0f

        val firstRow = ArrayList<String>()
        firstRow.add("Nimetus")
        firstRow.add("Kogus")
        firstRow.add("Ühik")
        firstRow.add("Ühikuhind")
        firstRow.add("Summa")

        for (tile in firstRow) {
            val cell = PdfPCell()
            cell.setPadding(3f)
            val headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f, BaseColor.BLACK)
            cell.backgroundColor = BaseColor.GRAY
            cell.horizontalAlignment = Element.ALIGN_LEFT
            cell.borderWidth = 0f
            cell.phrase = Phrase(tile, headFont)
            table.addCell(cell)
        }

        val bodyData = ArrayList<String>()
        bodyData.add("Kuulutus")
        bodyData.add(deal.duration.toString())
        bodyData.add("Päev")
        bodyData.add("%.4f".format((deal.cost.toDouble() / deal.duration.toDouble()) * 0.8))
        bodyData.add("%.2f".format(deal.cost.toDouble() * 0.8))

        return formatTable(bodyData, table)
    }

    private fun formatTable(data: ArrayList<String>, table: PdfPTable): PdfPTable {
        for (tile in data) {
            val cell = PdfPCell()
            val headFont = FontFactory.getFont(FontFactory.HELVETICA, 10f, BaseColor.BLACK)
            cell.horizontalAlignment = Element.ALIGN_LEFT
            cell.borderWidth = 0f
            cell.phrase = Phrase(tile, headFont)
            table.addCell(cell)
        }
        return table
    }

    fun getBodyFooter(deal: Deal): PdfPTable {
        val table = PdfPTable(5)
        table.widthPercentage = 100.0f

        val bodyFooterData = ArrayList<String>()
        bodyFooterData.add(" ")
        bodyFooterData.add(" ")
        bodyFooterData.add(" ")
        bodyFooterData.add("Summa kokku:")
        bodyFooterData.add("%.2f".format(deal.cost.toDouble() * 0.8) + " EUR")

        bodyFooterData.add(" ")
        bodyFooterData.add(" ")
        bodyFooterData.add(" ")
        bodyFooterData.add("Käibemaks 20%:")
        bodyFooterData.add("%.2f".format(deal.cost.toDouble() * 0.2) + " EUR")

        bodyFooterData.add(" ")
        bodyFooterData.add(" ")
        bodyFooterData.add(" ")
        bodyFooterData.add("TASUDA:")
        bodyFooterData.add("%.2f".format(deal.cost.toDouble()) + " EUR")

        for (tile in bodyFooterData) {
            val cell = PdfPCell()
            cell.setPadding(3f)
            val headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12f, BaseColor.BLACK)
            cell.horizontalAlignment = Element.ALIGN_RIGHT
            cell.borderWidth = 0f
            cell.phrase = Phrase(tile, headFont)
            table.addCell(cell)
        }

        return table
    }

    fun getInvoiceFooter(): PdfPTable {
        val table = PdfPTable(3)
        table.widthPercentage = 100.0f

        val footerData = ArrayList<String>()
        footerData.add("StudentPraxis")
        footerData.add("Telefon: xxxxxxxxx ")
        footerData.add("Pank: Swedbank AS")

        footerData.add("Aadress xxxxxxxxx")
        footerData.add("www.studentpraxis.ee")
        footerData.add("Liivalaia tn 8 Tallinn 15040")

        footerData.add("Tallinn zip, Eesti")
        footerData.add("info@studentpraxis.ee")
        footerData.add("IBAN: xxxxxxxxxxxx")

        footerData.add("Reg nr.xxxxxxxxxxxxxx")
        footerData.add(" ")
        footerData.add("SWIFT: xxxxxxxxxxxx")

        footerData.add("KMKR nr xxxxxxxxxxxxxx")
        footerData.add(" ")
        footerData.add(" ")


        for (i in 0 until footerData.size) {
            val tile = footerData[i]
            val cell = PdfPCell()
            var font = FontFactory.getFont(FontFactory.HELVETICA, 10f, BaseColor.BLACK)
            if (i == 0) {
                font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f, BaseColor.BLACK)
            }
            if (i % 3 == 0) cell.horizontalAlignment = Element.ALIGN_LEFT
            if (i % 3 == 1) cell.horizontalAlignment = Element.ALIGN_CENTER
            if (i % 3 == 2) cell.horizontalAlignment = Element.ALIGN_RIGHT

            cell.borderWidth = 0f
            cell.phrase = Phrase(tile, font)
            table.addCell(cell)
        }

        return table
    }
}