int scanf(const char *__restrict format, ...);
int puts(const char *str);
int printf(const char *format, ...);
int sprintf(char *__restrict s, const char *__restrict format, ...);
unsigned long strlen(const char *s);
void *malloc(unsigned long size);
void *calloc(unsigned long num, unsigned long size);
char *strcpy(char *dest, const char *src);
void free(void *ptr);
char *strcat(char *, const char *);
int strcmp(const char *, const char *);
void println(const char *str) {
  puts(str);
  return;
}

void print(const char *str) {
    printf("%s", str);
  return;
}

void printInt(int x) {
  printf("%d", x);
  return;
}

void printIntln(int x) {
  printf("%d\n", x);
  return;
}

char *toString(int x) {
  char *buffer = (char *)malloc(15);
  sprintf(buffer, "%d", x);
  char *output = (char *)malloc(strlen(buffer) + 1);
  strcpy(output, buffer);
  free(buffer);
  return output;
}

int getInt() {
  int x;
  scanf("%d", &x);
  return x;
}

char *getString() {
  char *buffer = (char *)malloc(4096);
  scanf("%s", buffer);
  char *output = (char *)malloc(strlen(buffer) + 1);
  strcpy(output, buffer);
  free(buffer);
  return output;
}

int string_length(char *str) { return strlen(str); }

char *string_substring(char *str, int left, int right) {
  char *res = (char *)malloc(sizeof(str) + 1);
  strcpy(res, str + left);
  *(res + (right - left)) = 0;
  char *output = (char *)malloc(strlen(res) + 1);
  strcpy(output, res);
  free(res);
  return output;
}

int string_parseInt(char *str) {
  int ans = 0;
  for (int i = 0; i < strlen(str); i++) {
    if ((*(str + i) >= '0') && (*(str + i) <= '9')) {
      ans *= 10;
      ans += *(str + i) - '0';
    } else {
      break;
    }
  }
  return ans;
}

int string_ord(char *str, int num) { return *(str + num); }

int string_cmp(char *str1, char *str2) { return strcmp(str1, str2); }

char *string_cat(char *str1, char *str2) { return strcat(str1, str2); }

void *ptr_array(int number) {
  int *res = (int *)malloc(number * sizeof(void *) + sizeof(int));
  *res = number;
  return res + 1;
}

int *int_array(int number) {
  int *res = (int *)malloc((number + 1) * sizeof(int));
  *res = number;
  return res + 1;
}

int array_size(void *ptr) { return *((int *)(ptr)-1); }

char *string_copy(char *str) {
  char *copy = (char *)malloc((strlen(str) + 1) * sizeof(char));
  strcpy(copy, str); 
  return copy;
}