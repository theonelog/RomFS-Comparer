/*
* Copyright 2024, theonelog
*
* Linux implementation of the file reading functionality. 
*/
#include <dirent.h>
#include <string.h>
#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <assert.h>

typedef struct fsdir{
    char *directory;
    int checksum;
} FS;

FS *createFile(char *dirpath, int checksm){
    FS *fp = malloc(sizeof(FS));
    assert(fp != NULL);
    strncpy(fp -> directory, dirpath, sizeof(dirpath));
    fp -> checksum = checksm;
    return fp;
}

void destroyFS(FS *file){
    free(file -> directory);
    free(file);
}

void getDir(const char *path, FILE *fp, FS *farr) {
    assert(fp != NULL);
    DIR *dir;
    struct dirent *ent;
    char full_path[PATH_MAX];

    dir = opendir(path);
    if (dir == NULL) {
        perror("opendir");
        return;
    }

    while ((ent = readdir(dir)) != NULL) {
        if (strcmp(ent->d_name, ".") == 0 || strcmp(ent->d_name, "..") == 0) {
            // skip . and ..
            continue;
        }

        sprintf(full_path, "%s/%s", path, ent->d_name);

        if (ent->d_type == DT_DIR) {
            // subdirectory, recurse
            getDir(full_path, fp, farr);
        } else {
            // file, print name
            strcat(full_path, "\n");
            fputs(full_path, fp);
        }
    }
    closedir(dir);
}

uint32_t calculate_checksum(const char *filename) {
    FILE *file = fopen(filename, "rb");
    if (file == NULL) {
        printf("Could not open file %s\n", filename);
        return 0;
    }

    uint32_t checksum = 0;
    uint8_t byte;

    while (fread(&byte, 1, 1, file) == 1) {
        checksum += byte;
    }

    fclose(file);
    return checksum;
}

int main() {
    FILE *existingList;
    FS *fileArray = malloc(sizeof(FS) * 10);
    existingList = fopen("s3files.txt", "w");
    char buffer[100];
    fgets(buffer, 100, stdin);
    char *trunk = (char *)malloc(strlen(buffer) * sizeof(char));
    assert(trunk != NULL);
    strncpy(trunk, buffer, strlen(buffer) - 1);
    getDir(trunk, existingList);
    printf("Sucessfully placed all directorires in the \"s3files.txt\" file...\n");
    free(trunk);    
    return 0;
}