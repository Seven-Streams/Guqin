@.str = private unnamed_addr constant [2 x i8] c"\0A\00", align 1, !dbg !0
@.str.1 = private unnamed_addr constant [3 x i8] c"%d\00", align 1, !dbg !8
@.str.2 = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1, !dbg !13
@.str.3 = private unnamed_addr constant [3 x i8] c"%s\00", align 1, !dbg !18

define dso_local void @println(char const*)(ptr noundef %0) #0 !dbg !34 {
  %2 = alloca ptr, align 4
  store ptr %0, ptr %2, align 4
    #dbg_declare(ptr %2, !39, !DIExpression(), !40)
  %3 = load ptr, ptr %2, align 4, !dbg !41
  %4 = call noundef i32 @puts(char const*)(ptr noundef %3), !dbg !42
  %5 = call noundef i32 @puts(char const*)(ptr noundef @.str), !dbg !43
  ret void, !dbg !44
}

declare dso_local noundef i32 @puts(char const*)(ptr noundef) #1

define dso_local void @print(char const*)(ptr noundef %0) #0 !dbg !45 {
  %2 = alloca ptr, align 4
  store ptr %0, ptr %2, align 4
    #dbg_declare(ptr %2, !46, !DIExpression(), !47)
  %3 = load ptr, ptr %2, align 4, !dbg !48
  %4 = call noundef i32 @puts(char const*)(ptr noundef %3), !dbg !49
  ret void, !dbg !50
}

define dso_local void @printInt(int)(i32 noundef %0) #0 !dbg !51 {
  %2 = alloca i32, align 4
  store i32 %0, ptr %2, align 4
    #dbg_declare(ptr %2, !55, !DIExpression(), !56)
  %3 = load i32, ptr %2, align 4, !dbg !57
  %4 = call noundef i32 (ptr, ...) @printf(char const*, ...)(ptr noundef @.str.1, i32 noundef %3), !dbg !58
  ret void, !dbg !59
}

declare dso_local noundef i32 @printf(char const*, ...)(ptr noundef, ...) #1

define dso_local void @printIntln(int)(i32 noundef %0) #0 !dbg !60 {
  %2 = alloca i32, align 4
  store i32 %0, ptr %2, align 4
    #dbg_declare(ptr %2, !61, !DIExpression(), !62)
  %3 = load i32, ptr %2, align 4, !dbg !63
  %4 = call noundef i32 (ptr, ...) @printf(char const*, ...)(ptr noundef @.str.2, i32 noundef %3), !dbg !64
  ret void, !dbg !65
}

define dso_local noundef ptr @toString(int)(i32 noundef %0) #0 !dbg !66 {
  %2 = alloca i32, align 4
  %3 = alloca ptr, align 4
  %4 = alloca ptr, align 4
  store i32 %0, ptr %2, align 4
    #dbg_declare(ptr %2, !69, !DIExpression(), !70)
    #dbg_declare(ptr %3, !71, !DIExpression(), !72)
  %5 = call noundef ptr @malloc(unsigned long)(i32 noundef 15), !dbg !73
  store ptr %5, ptr %3, align 4, !dbg !72
  %6 = load ptr, ptr %3, align 4, !dbg !74
  %7 = load i32, ptr %2, align 4, !dbg !75
  %8 = call noundef i32 (ptr, ptr, ...) @sprintf(char*, char const*, ...)(ptr noundef %6, ptr noundef @.str.1, i32 noundef %7), !dbg !76
    #dbg_declare(ptr %4, !77, !DIExpression(), !78)
  %9 = load ptr, ptr %3, align 4, !dbg !79
  %10 = call noundef i32 @strlen(char const*)(ptr noundef %9), !dbg !80
  %11 = add i32 %10, 1, !dbg !81
  %12 = call noundef ptr @malloc(unsigned long)(i32 noundef %11), !dbg !82
  store ptr %12, ptr %4, align 4, !dbg !78
  %13 = load ptr, ptr %4, align 4, !dbg !83
  %14 = load ptr, ptr %3, align 4, !dbg !84
  %15 = call noundef ptr @strcpy(char*, char const*)(ptr noundef %13, ptr noundef %14), !dbg !85
  %16 = load ptr, ptr %3, align 4, !dbg !86
  call void @free(void*)(ptr noundef %16), !dbg !87
  %17 = load ptr, ptr %4, align 4, !dbg !88
  ret ptr %17, !dbg !89
}

declare dso_local noundef ptr @malloc(unsigned long)(i32 noundef) #1

declare dso_local noundef i32 @sprintf(char*, char const*, ...)(ptr noundef, ptr noundef, ...) #1

declare dso_local noundef i32 @strlen(char const*)(ptr noundef) #1

declare dso_local noundef ptr @strcpy(char*, char const*)(ptr noundef, ptr noundef) #1

declare dso_local void @free(void*)(ptr noundef) #1

define dso_local noundef i32 @getInt()() #0 !dbg !90 {
  %1 = alloca i32, align 4
    #dbg_declare(ptr %1, !93, !DIExpression(), !94)
  %2 = call noundef i32 (ptr, ...) @scanf(char const*, ...)(ptr noundef @.str.1, ptr noundef %1), !dbg !95
  %3 = load i32, ptr %1, align 4, !dbg !96
  ret i32 %3, !dbg !97
}

declare dso_local noundef i32 @scanf(char const*, ...)(ptr noundef, ...) #1

define dso_local noundef ptr @getString()() #0 !dbg !98 {
  %1 = alloca ptr, align 4
  %2 = alloca ptr, align 4
    #dbg_declare(ptr %1, !100, !DIExpression(), !101)
  %3 = call noundef ptr @malloc(unsigned long)(i32 noundef 4096), !dbg !102
  store ptr %3, ptr %1, align 4, !dbg !101
  %4 = load ptr, ptr %1, align 4, !dbg !103
  %5 = call noundef i32 (ptr, ...) @scanf(char const*, ...)(ptr noundef @.str.3, ptr noundef %4), !dbg !104
    #dbg_declare(ptr %2, !105, !DIExpression(), !106)
  %6 = load ptr, ptr %1, align 4, !dbg !107
  %7 = call noundef i32 @strlen(char const*)(ptr noundef %6), !dbg !108
  %8 = add i32 %7, 1, !dbg !109
  %9 = call noundef ptr @malloc(unsigned long)(i32 noundef %8), !dbg !110
  store ptr %9, ptr %2, align 4, !dbg !106
  %10 = load ptr, ptr %2, align 4, !dbg !111
  %11 = load ptr, ptr %1, align 4, !dbg !112
  %12 = call noundef ptr @strcpy(char*, char const*)(ptr noundef %10, ptr noundef %11), !dbg !113
  %13 = load ptr, ptr %1, align 4, !dbg !114
  call void @free(void*)(ptr noundef %13), !dbg !115
  %14 = load ptr, ptr %2, align 4, !dbg !116
  ret ptr %14, !dbg !117
}

define dso_local noundef i32 @string_length(char*)(ptr noundef %0) #0 !dbg !118 {
  %2 = alloca ptr, align 4
  store ptr %0, ptr %2, align 4
    #dbg_declare(ptr %2, !121, !DIExpression(), !122)
  %3 = load ptr, ptr %2, align 4, !dbg !123
  %4 = call noundef i32 @strlen(char const*)(ptr noundef %3), !dbg !124
  ret i32 %4, !dbg !125
}

define dso_local noundef ptr @string_substring(char*, int, int)(ptr noundef %0, i32 noundef %1, i32 noundef %2) #0 !dbg !126 {
  %4 = alloca ptr, align 4
  %5 = alloca i32, align 4
  %6 = alloca i32, align 4
  %7 = alloca ptr, align 4
  %8 = alloca ptr, align 4
  store ptr %0, ptr %4, align 4
    #dbg_declare(ptr %4, !129, !DIExpression(), !130)
  store i32 %1, ptr %5, align 4
    #dbg_declare(ptr %5, !131, !DIExpression(), !132)
  store i32 %2, ptr %6, align 4
    #dbg_declare(ptr %6, !133, !DIExpression(), !134)
    #dbg_declare(ptr %7, !135, !DIExpression(), !136)
  %9 = call noundef ptr @malloc(unsigned long)(i32 noundef 5), !dbg !137
  store ptr %9, ptr %7, align 4, !dbg !136
  %10 = load ptr, ptr %7, align 4, !dbg !138
  %11 = load ptr, ptr %4, align 4, !dbg !139
  %12 = load i32, ptr %5, align 4, !dbg !140
  %13 = getelementptr inbounds i8, ptr %11, i32 %12, !dbg !141
  %14 = call noundef ptr @strcpy(char*, char const*)(ptr noundef %10, ptr noundef %13), !dbg !142
  %15 = load ptr, ptr %7, align 4, !dbg !143
  %16 = load i32, ptr %6, align 4, !dbg !144
  %17 = load i32, ptr %5, align 4, !dbg !145
  %18 = sub nsw i32 %16, %17, !dbg !146
  %19 = getelementptr inbounds i8, ptr %15, i32 %18, !dbg !147
  store i8 0, ptr %19, align 1, !dbg !148
    #dbg_declare(ptr %8, !149, !DIExpression(), !150)
  %20 = load ptr, ptr %7, align 4, !dbg !151
  %21 = call noundef i32 @strlen(char const*)(ptr noundef %20), !dbg !152
  %22 = add i32 %21, 1, !dbg !153
  %23 = call noundef ptr @malloc(unsigned long)(i32 noundef %22), !dbg !154
  store ptr %23, ptr %8, align 4, !dbg !150
  %24 = load ptr, ptr %8, align 4, !dbg !155
  %25 = load ptr, ptr %7, align 4, !dbg !156
  %26 = call noundef ptr @strcpy(char*, char const*)(ptr noundef %24, ptr noundef %25), !dbg !157
  %27 = load ptr, ptr %7, align 4, !dbg !158
  call void @free(void*)(ptr noundef %27), !dbg !159
  %28 = load ptr, ptr %8, align 4, !dbg !160
  ret ptr %28, !dbg !161
}

define dso_local noundef i32 @string_parseInt(char*)(ptr noundef %0) #0 !dbg !162 {
  %2 = alloca ptr, align 4
  %3 = alloca i32, align 4
  %4 = alloca i32, align 4
  store ptr %0, ptr %2, align 4
    #dbg_declare(ptr %2, !163, !DIExpression(), !164)
    #dbg_declare(ptr %3, !165, !DIExpression(), !166)
  store i32 0, ptr %3, align 4, !dbg !166
    #dbg_declare(ptr %4, !167, !DIExpression(), !169)
  store i32 0, ptr %4, align 4, !dbg !169
  br label %5, !dbg !170

5:
  %6 = load i32, ptr %4, align 4, !dbg !171
  %7 = load ptr, ptr %2, align 4, !dbg !173
  %8 = call noundef i32 @strlen(char const*)(ptr noundef %7), !dbg !174
  %9 = icmp ult i32 %6, %8, !dbg !175
  br i1 %9, label %10, label %40, !dbg !176

10:
  %11 = load ptr, ptr %2, align 4, !dbg !177
  %12 = load i32, ptr %4, align 4, !dbg !180
  %13 = getelementptr inbounds i8, ptr %11, i32 %12, !dbg !181
  %14 = load i8, ptr %13, align 1, !dbg !182
  %15 = zext i8 %14 to i32, !dbg !182
  %16 = icmp sge i32 %15, 48, !dbg !183
  br i1 %16, label %17, label %35, !dbg !184

17:
  %18 = load ptr, ptr %2, align 4, !dbg !185
  %19 = load i32, ptr %4, align 4, !dbg !186
  %20 = getelementptr inbounds i8, ptr %18, i32 %19, !dbg !187
  %21 = load i8, ptr %20, align 1, !dbg !188
  %22 = zext i8 %21 to i32, !dbg !188
  %23 = icmp sle i32 %22, 57, !dbg !189
  br i1 %23, label %24, label %35, !dbg !190

24:
  %25 = load i32, ptr %3, align 4, !dbg !191
  %26 = mul nsw i32 %25, 10, !dbg !191
  store i32 %26, ptr %3, align 4, !dbg !191
  %27 = load ptr, ptr %2, align 4, !dbg !193
  %28 = load i32, ptr %4, align 4, !dbg !194
  %29 = getelementptr inbounds i8, ptr %27, i32 %28, !dbg !195
  %30 = load i8, ptr %29, align 1, !dbg !196
  %31 = zext i8 %30 to i32, !dbg !196
  %32 = sub nsw i32 %31, 48, !dbg !197
  %33 = load i32, ptr %3, align 4, !dbg !198
  %34 = add nsw i32 %33, %32, !dbg !198
  store i32 %34, ptr %3, align 4, !dbg !198
  br label %36, !dbg !199

35:
  br label %40, !dbg !200

36:
  br label %37, !dbg !202

37:
  %38 = load i32, ptr %4, align 4, !dbg !203
  %39 = add nsw i32 %38, 1, !dbg !203
  store i32 %39, ptr %4, align 4, !dbg !203
  br label %5, !dbg !204

40:
  %41 = load i32, ptr %3, align 4, !dbg !208
  ret i32 %41, !dbg !209
}

define dso_local noundef i32 @string_ord(char*, int)(ptr noundef %0, i32 noundef %1) #2 !dbg !210 {
  %3 = alloca ptr, align 4
  %4 = alloca i32, align 4
  store ptr %0, ptr %3, align 4
    #dbg_declare(ptr %3, !213, !DIExpression(), !214)
  store i32 %1, ptr %4, align 4
    #dbg_declare(ptr %4, !215, !DIExpression(), !216)
  %5 = load ptr, ptr %3, align 4, !dbg !217
  %6 = load i32, ptr %4, align 4, !dbg !218
  %7 = getelementptr inbounds i8, ptr %5, i32 %6, !dbg !219
  %8 = load i8, ptr %7, align 1, !dbg !220
  %9 = zext i8 %8 to i32, !dbg !220
  ret i32 %9, !dbg !221
}

define dso_local noundef i32 @string_cmp(char*, char*)(ptr noundef %0, ptr noundef %1) #0 !dbg !222 {
  %3 = alloca ptr, align 4
  %4 = alloca ptr, align 4
  store ptr %0, ptr %3, align 4
    #dbg_declare(ptr %3, !225, !DIExpression(), !226)
  store ptr %1, ptr %4, align 4
    #dbg_declare(ptr %4, !227, !DIExpression(), !228)
  %5 = load ptr, ptr %3, align 4, !dbg !229
  %6 = load ptr, ptr %4, align 4, !dbg !230
  %7 = call noundef i32 @strcmp(char const*, char const*)(ptr noundef %5, ptr noundef %6), !dbg !231
  ret i32 %7, !dbg !232
}

declare dso_local noundef i32 @strcmp(char const*, char const*)(ptr noundef, ptr noundef) #1

define dso_local noundef ptr @string_cat(char*, char*)(ptr noundef %0, ptr noundef %1) #0 !dbg !233 {
  %3 = alloca ptr, align 4
  %4 = alloca ptr, align 4
  store ptr %0, ptr %3, align 4
    #dbg_declare(ptr %3, !236, !DIExpression(), !237)
  store ptr %1, ptr %4, align 4
    #dbg_declare(ptr %4, !238, !DIExpression(), !239)
  %5 = load ptr, ptr %3, align 4, !dbg !240
  %6 = load ptr, ptr %4, align 4, !dbg !241
  %7 = call noundef ptr @strcat(char*, char const*)(ptr noundef %5, ptr noundef %6), !dbg !242
  ret ptr %7, !dbg !243
}

declare dso_local noundef ptr @strcat(char*, char const*)(ptr noundef, ptr noundef) #1

define dso_local noundef i32 @main() #3 !dbg !244 {
  %1 = call noundef ptr @getString()(), !dbg !245
  ret i32 0, !dbg !246
}

attributes #0 = { mustprogress noinline optnone "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+d,+f,+m,+relax,+zicsr,+zifencei,+zmmul,-b,-e,-experimental-smmpm,-experimental-smnpm,-experimental-ssnpm,-experimental-sspm,-experimental-ssqosid,-experimental-supm,-experimental-zacas,-experimental-zalasr,-experimental-zicfilp,-experimental-zicfiss,-h,-shcounterenw,-shgatpa,-shtvala,-shvsatpa,-shvstvala,-shvstvecd,-smaia,-smcdeleg,-smcsrind,-smepmp,-smstateen,-ssaia,-ssccfg,-ssccptr,-sscofpmf,-sscounterenw,-sscsrind,-ssstateen,-ssstrict,-sstc,-sstvala,-sstvecd,-ssu64xl,-svade,-svadu,-svbare,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfcease,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xsifivecdiscarddlone,-xsifivecflushdlone,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-xwchc,-za128rs,-za64rs,-zaamo,-zabha,-zalrsc,-zama16b,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmop,-zcmp,-zcmt,-zdinx,-zfa,-zfbfmin,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zihintntl,-zihintpause,-zihpm,-zimop,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-ztso,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfbfmin,-zvfbfwma,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #1 = { "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+d,+f,+m,+relax,+zicsr,+zifencei,+zmmul,-b,-e,-experimental-smmpm,-experimental-smnpm,-experimental-ssnpm,-experimental-sspm,-experimental-ssqosid,-experimental-supm,-experimental-zacas,-experimental-zalasr,-experimental-zicfilp,-experimental-zicfiss,-h,-shcounterenw,-shgatpa,-shtvala,-shvsatpa,-shvstvala,-shvstvecd,-smaia,-smcdeleg,-smcsrind,-smepmp,-smstateen,-ssaia,-ssccfg,-ssccptr,-sscofpmf,-sscounterenw,-sscsrind,-ssstateen,-ssstrict,-sstc,-sstvala,-sstvecd,-ssu64xl,-svade,-svadu,-svbare,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfcease,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xsifivecdiscarddlone,-xsifivecflushdlone,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-xwchc,-za128rs,-za64rs,-zaamo,-zabha,-zalrsc,-zama16b,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmop,-zcmp,-zcmt,-zdinx,-zfa,-zfbfmin,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zihintntl,-zihintpause,-zihpm,-zimop,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-ztso,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfbfmin,-zvfbfwma,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #2 = { mustprogress noinline nounwind optnone "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+d,+f,+m,+relax,+zicsr,+zifencei,+zmmul,-b,-e,-experimental-smmpm,-experimental-smnpm,-experimental-ssnpm,-experimental-sspm,-experimental-ssqosid,-experimental-supm,-experimental-zacas,-experimental-zalasr,-experimental-zicfilp,-experimental-zicfiss,-h,-shcounterenw,-shgatpa,-shtvala,-shvsatpa,-shvstvala,-shvstvecd,-smaia,-smcdeleg,-smcsrind,-smepmp,-smstateen,-ssaia,-ssccfg,-ssccptr,-sscofpmf,-sscounterenw,-sscsrind,-ssstateen,-ssstrict,-sstc,-sstvala,-sstvecd,-ssu64xl,-svade,-svadu,-svbare,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfcease,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xsifivecdiscarddlone,-xsifivecflushdlone,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-xwchc,-za128rs,-za64rs,-zaamo,-zabha,-zalrsc,-zama16b,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmop,-zcmp,-zcmt,-zdinx,-zfa,-zfbfmin,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zihintntl,-zihintpause,-zihpm,-zimop,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-ztso,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfbfmin,-zvfbfwma,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #3 = { mustprogress noinline norecurse optnone "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+d,+f,+m,+relax,+zicsr,+zifencei,+zmmul,-b,-e,-experimental-smmpm,-experimental-smnpm,-experimental-ssnpm,-experimental-sspm,-experimental-ssqosid,-experimental-supm,-experimental-zacas,-experimental-zalasr,-experimental-zicfilp,-experimental-zicfiss,-h,-shcounterenw,-shgatpa,-shtvala,-shvsatpa,-shvstvala,-shvstvecd,-smaia,-smcdeleg,-smcsrind,-smepmp,-smstateen,-ssaia,-ssccfg,-ssccptr,-sscofpmf,-sscounterenw,-sscsrind,-ssstateen,-ssstrict,-sstc,-sstvala,-sstvecd,-ssu64xl,-svade,-svadu,-svbare,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfcease,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xsifivecdiscarddlone,-xsifivecflushdlone,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-xwchc,-za128rs,-za64rs,-zaamo,-zabha,-zalrsc,-zama16b,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmop,-zcmp,-zcmt,-zdinx,-zfa,-zfbfmin,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zihintntl,-zihintpause,-zihpm,-zimop,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-ztso,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfbfmin,-zvfbfwma,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
;According to the manual, this part is generated by Clang, written with C.