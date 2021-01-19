#!/bin/bash

export TERM=xterm-256color
echo $TERM

# Reset
export Color_Off='\033[0m'       # Text Reset

# Regular Colors
export Black='\033[0;30m'        # Black
export Red='\033[0;31m'          # Red
export Green='\033[0;32m'        # Green
export Yellow='\033[0;33m'       # Yellow
export Blue='\033[0;34m'         # Blue
export Purple='\033[0;35m'       # Purple
export Cyan='\033[0;36m'         # Cyan

# High Intensity
export IBlack='\033[0;90m'       # Black
export IRed='\033[0;91m'         # Red
export IGreen='\033[0;92m'       # Green
export IYellow='\033[0;93m'      # Yellow
export IBlue='\033[0;94m'        # Blue
export IPurple='\033[0;95m'      # Purple
export ICyan='\033[0;96m'        # Cyan
export IWhite='\033[0;97m'       # White

# Bold High Intensity
export BIBlack='\033[1;90m'      # Black
export BIRed='\033[1;91m'        # Red
export BIGreen='\033[1;92m'      # Green
export BIYellow='\033[1;93m'     # Yellow
export BIBlue='\033[1;94m'       # Blue
export BIPurple='\033[1;95m'     # Purple
export BICyan='\033[1;96m'       # Cyan
export BIWhite='\033[1;97m'      # White

export DATABASE_CONTAINER_NAME="postgres-container"
echo -e "${IYellow}"
docker container ls -a -s

if [ ! "$(docker ps -q -f name="$DATABASE_CONTAINER_NAME")" ]; then
    if [ "$(docker ps -aq -f status=exited -f name="$DATABASE_CONTAINER_NAME")" ]; then
        # cleanup
        echo -e "${Purple}"
        docker rm "$DATABASE_CONTAINER_NAME"
    fi

    echo -e "${BICyan}Pulling postgres${Blue}"
    docker pull postgres

    echo -e "${Yellow}"
    docker container ls -a -s
    echo -e "${BICyan}Stopping container:${BIYellow} $DATABASE_CONTAINER_NAME ${IPurple}"
    docker stop "$DATABASE_CONTAINER_NAME" || true
    echo -e "${BICyan}Removing container:${BIYellow} $DATABASE_CONTAINER_NAME ${IPurple}"
    docker rm "$DATABASE_CONTAINER_NAME" || true

    echo -e "${Green}"
    docker container ls -a -s

    echo -e "${BICyan}Creating internal network bridge for proxy-back-database (if none exsists) ${IRed}"
    docker network create --driver bridge api-internal-network || true # Create only if none exists

    export DB_PASS=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1)

    echo -e "${BICyan}Starting${BIYellow} $DATABASE_CONTAINER_NAME ${IPurple}"
    docker run \
        -e "POSTGRES_USER=postgres" \
        -e "POSTGRES_PASSWORD=$DB_PASS" \
        -e "POSTGRES_DB=sp_db" \
        --name "$DATABASE_CONTAINER_NAME" \
        --network "api-internal-network" \
        -p 5432:5432 \
        --restart=always \
        -v ~/postgres-data:/var/lib/postgresql/data \
        -d "postgres" \
        # -u "postgres" # Will own data folders

    echo -e "${Yellow}"
    docker container ls -a -s
    echo -e "${BIGreen}Generated new password for database, make sure to write it down!${BPurple}"
    echo "$DB_PASS"
    echo -e "${Color_Off}"
else
    echo -e "${BIYellow}$DATABASE_CONTAINER_NAME ${BICyan}seems to be running.${Color_Off}"
fi