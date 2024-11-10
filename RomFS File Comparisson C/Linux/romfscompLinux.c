#include <dirent.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

void print_dir(const char *path, FILE *fp) {
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
            print_dir(full_path, fp);
        } else {
            // file, print name
            strcat(full_path, "\n");
            fputs(full_path, fp);
        }
    }

    closedir(dir);
}

int main() {
    FILE *existingList;
    existingList = fopen("s3files.txt", "w");
    print_dir("/mnt/d/Console Games/RomFS/Splatoon 3 9.1.0 RomFS", existingList);
    return 0;
}