#include "dirent.h"
#include <Windows.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>

char** print_dir(const char *path) {
    DIR *dir;
    struct dirent *ent;
    char full_path[PATH_MAX];
    char *directories[PATH_MAX];

    dir = opendir(path);
    if (dir == NULL) {
        perror("opendir");
        return;
    }
    int i = 0;
    while ((ent = readdir(dir)) != NULL) {
        if (strcmp(ent->d_name, ".") == 0 || strcmp(ent->d_name, "..") == 0) {
            // skip . and ..
            continue;
        }

        sprintf(full_path, "%s/%s", path, ent->d_name);

        if (ent->d_type == DT_DIR) {
            // subdirectory, recurse
            print_dir(full_path);
        } else {
            // file, print name
            directories[i++] = strdup(full_path);
        }
    }
    return directories;
    closedir(dir);
}

int main() {
    char **directories = print_dir("/mnt/d/Console Games/RomFS/Splatoon 3 9.1.0 RomFS");
    for(int i = 0; i < sizeof(directories)/sizeof(directories[0]); i++){
        printf("%s\n", directories[i]);
    }
    return 0;
}