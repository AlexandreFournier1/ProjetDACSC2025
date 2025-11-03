#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <netdb.h>
#include <sys/socket.h>

int main() {
    struct addrinfo hints, *res;
    int sfd;
    memset(&hints, 0, sizeof(hints));
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_STREAM;

    if (getaddrinfo("127.0.0.1", "50000", &hints, &res) != 0) {
        perror("getaddrinfo");
        return 1;
    }

    sfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);
    if (sfd < 0) { perror("socket"); return 1; }

    printf("Avant connect(); fd=%d, addrlen=%zu\n", sfd, (size_t)res->ai_addrlen);
    fflush(stdout);

    if (connect(sfd, res->ai_addr, res->ai_addrlen) == -1) {
        perror("connect");
        return 1;
    }
    printf("Connect OK\n");
    close(sfd);
    freeaddrinfo(res);
    return 0;
}