--  ==================== CLASSIFIERS ====================

CREATE TABLE IF NOT EXISTS sp.user_role
(
    role_id SERIAL PRIMARY KEY,
    role    VARCHAR(255) NOT NULL,

    CONSTRAINT ak_role UNIQUE (role)
);

INSERT INTO sp.user_role(role_id, role)
VALUES (1, 'ROLE_REGULAR_USER'),
       (2, 'ROLE_ADMIN');

CREATE TABLE IF NOT EXISTS sp.contact_type
(
    contact_type_id SERIAL PRIMARY KEY,
    type            VARCHAR(255) NOT NULL,

    CONSTRAINT ak_contact_type UNIQUE (type)
);

INSERT INTO sp.contact_type(contact_type_id, type)
VALUES (1, 'EMAIL'),
       (2, 'PHONE'),
       (3, 'LINKED_IN'),
       (4, 'ADDRESS');

CREATE TABLE IF NOT EXISTS sp.invoice_status
(
    invoice_status_id SERIAL PRIMARY KEY,
    status            VARCHAR(255) NOT NULL,

    CONSTRAINT ak_status UNIQUE (status)
);

INSERT INTO sp.invoice_status(invoice_status_id, status)
VALUES (1, 'GENERATED'),
       (2, 'SENT'),
       (3, 'PAID'),
       (4, 'EXPIRED'),
       (5, 'INVALID');

CREATE TABLE IF NOT EXISTS sp.internship_category
(
    internship_category_id SERIAL PRIMARY KEY,
    category               VARCHAR(255) NOT NULL,

    CONSTRAINT ak_internship UNIQUE (category)
);

INSERT INTO sp.internship_category(category)
VALUES ('Õigus'),
       ('IT'),
       ('Ehitus / kinnisvara'),
       ('Meedia'),
       ('Teenindus / toitlustus'),
       ('Müük- ja turundus'),
       ('Meditsiin'),
       ('Finants'),
       ('Logistika'),
       ('Kultuur / meelelahutus'),
       ('Turism'),
       ('Tehnika / mehhaanika'),
       ('Muu');

CREATE TABLE IF NOT EXISTS sp.deal_status
(
    deal_status_id SERIAL PRIMARY KEY,
    status         VARCHAR(255) NOT NULL,

    CONSTRAINT ak_deal_status UNIQUE (status)
);

INSERT INTO sp.deal_status(deal_status_id, status)
VALUES (1, 'INITIALIZED'),
       (2, 'ACTIVE'),
       (3, 'NOT ACTIVE'),
       (4, 'DELETED');

-- ================== END OF CLASSIFIERS ==================

-- ====================== MAIN TABLES =====================

CREATE TABLE IF NOT EXISTS sp.user
(
    user_id  SERIAL PRIMARY KEY,
    email    VARCHAR(255)      NOT NULL,
    password VARCHAR(255)      NOT NULL,
    reg_date TIMESTAMP         NOT NULL,
    image    VARCHAR(255)      NULL,
    role_id  INTEGER DEFAULT 1 NOT NULL,

    CONSTRAINT ak_email UNIQUE (email),
    CONSTRAINT ak_image UNIQUE (image),

    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES sp.user_role (role_id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS sp.internship_provider
(
    internship_provider_id SERIAL PRIMARY KEY,
    registry_code          INTEGER      NOT NULL,
    company_name           VARCHAR(255) NOT NULL,
    image                  VARCHAR(255) NULL,
    introduction           TEXT         NULL
);

CREATE TABLE IF NOT EXISTS sp.intern
(
    user_id   SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    about     TEXT         NULL,

    CONSTRAINT fk_intern_user FOREIGN KEY (user_id) REFERENCES sp.user (user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sp.user_contact
(
    contact_id        SERIAL PRIMARY KEY,
    user_id           INTEGER      NOT NULL,
    contact_type_id   INTEGER      NOT NULL,
    value             VARCHAR(255) NOT NULL,
    created_timestamp TIMESTAMP    NOT NULL,

    CONSTRAINT fk_user_contact_user FOREIGN KEY (user_id) REFERENCES sp.user (user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_user_contact_contact_type FOREIGN KEY (contact_type_id) REFERENCES sp.contact_type (contact_type_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS sp.company_contact
(
    contact_id        SERIAL PRIMARY KEY,
    company_id        INTEGER      NOT NULL,
    contact_type_id   INTEGER      NOT NULL,
    value             VARCHAR(255) NOT NULL,
    created_timestamp TIMESTAMP    NOT NULL,

    CONSTRAINT fk_company_contact_company FOREIGN KEY (company_id) REFERENCES sp.internship_provider (internship_provider_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_company_contact_contact_type FOREIGN KEY (contact_type_id) REFERENCES sp.contact_type (contact_type_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS sp.curriculum_vitae
(
    curriculum_vitae_id SERIAL PRIMARY KEY,
    user_id             INTEGER      NOT NULL,
    upload_time         TIMESTAMP    NOT NULL,
    file                VARCHAR(255) NOT NULL,

    CONSTRAINT fk_curriculum_vitae_user FOREIGN KEY (user_id) REFERENCES sp.user (user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sp.advertisement
(
    advertisement_id       SERIAL PRIMARY KEY,
    internship_provider_id INTEGER      NOT NULL,
    internship_title       VARCHAR(255) NOT NULL,
    image                  VARCHAR(255) NULL,
    text                   TEXT         NULL,
    created_timestamp      TIMESTAMP    NOT NULL,
    start_timestamp        TIMESTAMP    NULL,
    end_timestamp          TIMESTAMP    NULL,

    CONSTRAINT fk_advertisement_internship_provider FOREIGN KEY (internship_provider_id) REFERENCES sp.internship_provider (internship_provider_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sp.candidature
(
    candidature_id         SERIAL PRIMARY KEY,
    intern_id              INTEGER   NOT NULL,
    advertisement_id       INTEGER   NOT NULL,
    timestamp              TIMESTAMP NOT NULL,
    message                TEXT      NULL,
    active                 BOOLEAN   NOT NULL,

    CONSTRAINT fk_candidature_intern FOREIGN KEY (intern_id) REFERENCES sp.intern (user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_candidature_advertisement FOREIGN KEY (advertisement_id) REFERENCES sp.advertisement (advertisement_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sp.deal
(
    deal_id   SERIAL PRIMARY KEY,
    status_id INTEGER NOT NULL,
    cost      INTEGER NOT NULL,
    duration  INTEGER NOT NULL,

    CONSTRAINT fk_deal_status FOREIGN KEY (status_id) REFERENCES sp.deal_status (deal_status_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);
INSERT INTO sp.deal(deal_id, status_id, cost, duration)
VALUES (1, 2, 10, 30);

CREATE TABLE IF NOT EXISTS sp.invoice
(
    invoice_id        SERIAL PRIMARY KEY,
    status_id         INTEGER      NOT NULL,
    deal_id           INTEGER      NOT NULL,
    pdf_file          VARCHAR(255) NOT NULL,
    created_timestamp TIMESTAMP    NOT NULL,

    CONSTRAINT fk_invoice_status FOREIGN KEY (status_id) REFERENCES sp.invoice_status (invoice_status_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_deal FOREIGN KEY (deal_id) REFERENCES sp.deal (deal_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sp.vanilla_candidature
(
    vanilla_candidature_id SERIAL PRIMARY KEY,
    advertisement_id       INTEGER   NOT NULL,
    timestamp              TIMESTAMP NOT NULL,
    email                  TEXT      NULL,
    curriculum_vitae       TEXT      NULL,
    linkedin               TEXT      NULL,

    CONSTRAINT fk_vanilla_candidature_advertisement FOREIGN KEY (advertisement_id) REFERENCES sp.advertisement (advertisement_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- ================== END OF MAIN TABLES ==================

-- =================== MEMBER TABLES ======================

CREATE TABLE IF NOT EXISTS sp.internship_provider_member
(
    user_id                INTEGER NOT NULL,
    internship_provider_id INTEGER NOT NULL,

    PRIMARY KEY (user_id, internship_provider_id),

    CONSTRAINT fk_internship_provider_member_user FOREIGN KEY (user_id) REFERENCES sp.user (user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_internship_provider_member_internship_provider FOREIGN KEY (internship_provider_id) REFERENCES sp.internship_provider (internship_provider_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sp.internship_category_member
(
    advertisement_id       INTEGER NOT NULL,
    internship_category_id INTEGER NOT NULL,

    PRIMARY KEY (advertisement_id, internship_category_id),

    CONSTRAINT fk_internship_category_member_advertisement FOREIGN KEY (advertisement_id) REFERENCES sp.advertisement (advertisement_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_internship_category_member_internship_category FOREIGN KEY (internship_category_id) REFERENCES sp.internship_category (internship_category_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sp.advertisement_invoice_member
(
    invoice_id       INTEGER NOT NULL,
    advertisement_id INTEGER NOT NULL,

    PRIMARY KEY (invoice_id, advertisement_id),

    CONSTRAINT fk_advertisement_invoice_member_invoice FOREIGN KEY (invoice_id) REFERENCES sp.invoice (invoice_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_advertisement_invoice_member_advertisement FOREIGN KEY (advertisement_id) REFERENCES sp.advertisement (advertisement_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);


-- ================= END OF MEMBER TABLES =================