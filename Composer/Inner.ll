@.str = private unnamed_addr constant [2 x i8] c"\0A\00", align 1, !dbg !0
@.str.1 = private unnamed_addr constant [3 x i8] c"%d\00", align 1, !dbg !8
@.str.2 = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1, !dbg !13
@.str.3 = private unnamed_addr constant [3 x i8] c"%s\00", align 1, !dbg !18

define dso_local void @println(char const*)(ptr noundef %0) #0 !dbg !36 {
  %2 = alloca ptr, align 4
  store ptr %0, ptr %2, align 4
    #dbg_declare(ptr %2, !41, !DIExpression(), !42)
  %3 = load ptr, ptr %2, align 4, !dbg !43
  %4 = call noundef i32 @puts(char const*)(ptr noundef %3), !dbg !44
  %5 = call noundef i32 @puts(char const*)(ptr noundef @.str), !dbg !45
  ret void, !dbg !46
}

declare dso_local noundef i32 @puts(char const*)(ptr noundef) #1

define dso_local void @print(char const*)(ptr noundef %0) #0 !dbg !47 {
  %2 = alloca ptr, align 4
  store ptr %0, ptr %2, align 4
    #dbg_declare(ptr %2, !48, !DIExpression(), !49)
  %3 = load ptr, ptr %2, align 4, !dbg !50
  %4 = call noundef i32 @puts(char const*)(ptr noundef %3), !dbg !51
  ret void, !dbg !52
}

define dso_local void @printInt(int)(i32 noundef %0) #0 !dbg !53 {
  %2 = alloca i32, align 4
  store i32 %0, ptr %2, align 4
    #dbg_declare(ptr %2, !56, !DIExpression(), !57)
  %3 = load i32, ptr %2, align 4, !dbg !58
  %4 = call noundef i32 (ptr, ...) @printf(char const*, ...)(ptr noundef @.str.1, i32 noundef %3), !dbg !59
  ret void, !dbg !60
}

declare dso_local noundef i32 @printf(char const*, ...)(ptr noundef, ...) #1

define dso_local void @printIntln(int)(i32 noundef %0) #0 !dbg !61 {
  %2 = alloca i32, align 4
  store i32 %0, ptr %2, align 4
    #dbg_declare(ptr %2, !62, !DIExpression(), !63)
  %3 = load i32, ptr %2, align 4, !dbg !64
  %4 = call noundef i32 (ptr, ...) @printf(char const*, ...)(ptr noundef @.str.2, i32 noundef %3), !dbg !65
  ret void, !dbg !66
}

define dso_local noundef ptr @toString(int)(i32 noundef %0) #0 !dbg !67 {
  %2 = alloca i32, align 4
  %3 = alloca ptr, align 4
  %4 = alloca ptr, align 4
  store i32 %0, ptr %2, align 4
    #dbg_declare(ptr %2, !70, !DIExpression(), !71)
    #dbg_declare(ptr %3, !72, !DIExpression(), !73)
  %5 = call noundef ptr @malloc(unsigned long)(i32 noundef 15), !dbg !74
  store ptr %5, ptr %3, align 4, !dbg !73
  %6 = load ptr, ptr %3, align 4, !dbg !75
  %7 = load i32, ptr %2, align 4, !dbg !76
  %8 = call noundef i32 (ptr, ptr, ...) @sprintf(char*, char const*, ...)(ptr noundef %6, ptr noundef @.str.1, i32 noundef %7), !dbg !77
    #dbg_declare(ptr %4, !78, !DIExpression(), !79)
  %9 = load ptr, ptr %3, align 4, !dbg !80
  %10 = call noundef i32 @strlen(char const*)(ptr noundef %9), !dbg !81
  %11 = add i32 %10, 1, !dbg !82
  %12 = call noundef ptr @malloc(unsigned long)(i32 noundef %11), !dbg !83
  store ptr %12, ptr %4, align 4, !dbg !79
  %13 = load ptr, ptr %4, align 4, !dbg !84
  %14 = load ptr, ptr %3, align 4, !dbg !85
  %15 = call noundef ptr @strcpy(char*, char const*)(ptr noundef %13, ptr noundef %14), !dbg !86
  %16 = load ptr, ptr %3, align 4, !dbg !87
  call void @free(void*)(ptr noundef %16), !dbg !88
  %17 = load ptr, ptr %4, align 4, !dbg !89
  ret ptr %17, !dbg !90
}

declare dso_local noundef ptr @malloc(unsigned long)(i32 noundef) #1

declare dso_local noundef i32 @sprintf(char*, char const*, ...)(ptr noundef, ptr noundef, ...) #1

declare dso_local noundef i32 @strlen(char const*)(ptr noundef) #1

declare dso_local noundef ptr @strcpy(char*, char const*)(ptr noundef, ptr noundef) #1

declare dso_local void @free(void*)(ptr noundef) #1

define dso_local noundef i32 @getInt()() #0 !dbg !91 {
  %1 = alloca i32, align 4
    #dbg_declare(ptr %1, !94, !DIExpression(), !95)
  %2 = call noundef i32 (ptr, ...) @scanf(char const*, ...)(ptr noundef @.str.1, ptr noundef %1), !dbg !96
  %3 = load i32, ptr %1, align 4, !dbg !97
  ret i32 %3, !dbg !98
}

declare dso_local noundef i32 @scanf(char const*, ...)(ptr noundef, ...) #1

define dso_local noundef ptr @getString()() #0 !dbg !99 {
  %1 = alloca ptr, align 4
  %2 = alloca ptr, align 4
    #dbg_declare(ptr %1, !102, !DIExpression(), !103)
  %3 = call noundef ptr @malloc(unsigned long)(i32 noundef 4096), !dbg !104
  store ptr %3, ptr %1, align 4, !dbg !103
  %4 = load ptr, ptr %1, align 4, !dbg !105
  %5 = call noundef i32 (ptr, ...) @scanf(char const*, ...)(ptr noundef @.str.3, ptr noundef %4), !dbg !106
    #dbg_declare(ptr %2, !107, !DIExpression(), !108)
  %6 = load ptr, ptr %1, align 4, !dbg !109
  %7 = call noundef i32 @strlen(char const*)(ptr noundef %6), !dbg !110
  %8 = add i32 %7, 1, !dbg !111
  %9 = call noundef ptr @malloc(unsigned long)(i32 noundef %8), !dbg !112
  store ptr %9, ptr %2, align 4, !dbg !108
  %10 = load ptr, ptr %2, align 4, !dbg !113
  %11 = load ptr, ptr %1, align 4, !dbg !114
  %12 = call noundef ptr @strcpy(char*, char const*)(ptr noundef %10, ptr noundef %11), !dbg !115
  %13 = load ptr, ptr %1, align 4, !dbg !116
  call void @free(void*)(ptr noundef %13), !dbg !117
  %14 = load ptr, ptr %2, align 4, !dbg !118
  ret ptr %14, !dbg !119
}

define dso_local noundef i32 @string_length(char*)(ptr noundef %0) #0 !dbg !120 {
  %2 = alloca ptr, align 4
  store ptr %0, ptr %2, align 4
    #dbg_declare(ptr %2, !123, !DIExpression(), !124)
  %3 = load ptr, ptr %2, align 4, !dbg !125
  %4 = call noundef i32 @strlen(char const*)(ptr noundef %3), !dbg !126
  ret i32 %4, !dbg !127
}

define dso_local noundef ptr @string_substring(char*, int, int)(ptr noundef %0, i32 noundef %1, i32 noundef %2) #0 !dbg !128 {
  %4 = alloca ptr, align 4
  %5 = alloca i32, align 4
  %6 = alloca i32, align 4
  %7 = alloca ptr, align 4
  %8 = alloca ptr, align 4
  store ptr %0, ptr %4, align 4
    #dbg_declare(ptr %4, !131, !DIExpression(), !132)
  store i32 %1, ptr %5, align 4
    #dbg_declare(ptr %5, !133, !DIExpression(), !134)
  store i32 %2, ptr %6, align 4
    #dbg_declare(ptr %6, !135, !DIExpression(), !136)
    #dbg_declare(ptr %7, !137, !DIExpression(), !138)
  %9 = call noundef ptr @malloc(unsigned long)(i32 noundef 5), !dbg !139
  store ptr %9, ptr %7, align 4, !dbg !138
  %10 = load ptr, ptr %7, align 4, !dbg !140
  %11 = load ptr, ptr %4, align 4, !dbg !141
  %12 = load i32, ptr %5, align 4, !dbg !142
  %13 = getelementptr inbounds i8, ptr %11, i32 %12, !dbg !143
  %14 = call noundef ptr @strcpy(char*, char const*)(ptr noundef %10, ptr noundef %13), !dbg !144
  %15 = load ptr, ptr %7, align 4, !dbg !145
  %16 = load i32, ptr %6, align 4, !dbg !146
  %17 = load i32, ptr %5, align 4, !dbg !147
  %18 = sub nsw i32 %16, %17, !dbg !148
  %19 = getelementptr inbounds i8, ptr %15, i32 %18, !dbg !149
  store i8 0, ptr %19, align 1, !dbg !150
    #dbg_declare(ptr %8, !151, !DIExpression(), !152)
  %20 = load ptr, ptr %7, align 4, !dbg !153
  %21 = call noundef i32 @strlen(char const*)(ptr noundef %20), !dbg !154
  %22 = add i32 %21, 1, !dbg !155
  %23 = call noundef ptr @malloc(unsigned long)(i32 noundef %22), !dbg !156
  store ptr %23, ptr %8, align 4, !dbg !152
  %24 = load ptr, ptr %8, align 4, !dbg !157
  %25 = load ptr, ptr %7, align 4, !dbg !158
  %26 = call noundef ptr @strcpy(char*, char const*)(ptr noundef %24, ptr noundef %25), !dbg !159
  %27 = load ptr, ptr %7, align 4, !dbg !160
  call void @free(void*)(ptr noundef %27), !dbg !161
  %28 = load ptr, ptr %8, align 4, !dbg !162
  ret ptr %28, !dbg !163
}

define dso_local noundef i32 @string_parseInt(char*)(ptr noundef %0) #0 !dbg !164 {
  %2 = alloca ptr, align 4
  %3 = alloca i32, align 4
  %4 = alloca i32, align 4
  store ptr %0, ptr %2, align 4
    #dbg_declare(ptr %2, !165, !DIExpression(), !166)
    #dbg_declare(ptr %3, !167, !DIExpression(), !168)
  store i32 0, ptr %3, align 4, !dbg !168
    #dbg_declare(ptr %4, !169, !DIExpression(), !171)
  store i32 0, ptr %4, align 4, !dbg !171
  br label %5, !dbg !172

5:
  %6 = load i32, ptr %4, align 4, !dbg !173
  %7 = load ptr, ptr %2, align 4, !dbg !175
  %8 = call noundef i32 @strlen(char const*)(ptr noundef %7), !dbg !176
  %9 = icmp ult i32 %6, %8, !dbg !177
  br i1 %9, label %10, label %40, !dbg !178

10:
  %11 = load ptr, ptr %2, align 4, !dbg !179
  %12 = load i32, ptr %4, align 4, !dbg !182
  %13 = getelementptr inbounds i8, ptr %11, i32 %12, !dbg !183
  %14 = load i8, ptr %13, align 1, !dbg !184
  %15 = zext i8 %14 to i32, !dbg !184
  %16 = icmp sge i32 %15, 48, !dbg !185
  br i1 %16, label %17, label %35, !dbg !186

17:
  %18 = load ptr, ptr %2, align 4, !dbg !187
  %19 = load i32, ptr %4, align 4, !dbg !188
  %20 = getelementptr inbounds i8, ptr %18, i32 %19, !dbg !189
  %21 = load i8, ptr %20, align 1, !dbg !190
  %22 = zext i8 %21 to i32, !dbg !190
  %23 = icmp sle i32 %22, 57, !dbg !191
  br i1 %23, label %24, label %35, !dbg !192

24:
  %25 = load i32, ptr %3, align 4, !dbg !193
  %26 = mul nsw i32 %25, 10, !dbg !193
  store i32 %26, ptr %3, align 4, !dbg !193
  %27 = load ptr, ptr %2, align 4, !dbg !195
  %28 = load i32, ptr %4, align 4, !dbg !196
  %29 = getelementptr inbounds i8, ptr %27, i32 %28, !dbg !197
  %30 = load i8, ptr %29, align 1, !dbg !198
  %31 = zext i8 %30 to i32, !dbg !198
  %32 = sub nsw i32 %31, 48, !dbg !199
  %33 = load i32, ptr %3, align 4, !dbg !200
  %34 = add nsw i32 %33, %32, !dbg !200
  store i32 %34, ptr %3, align 4, !dbg !200
  br label %36, !dbg !201

35:
  br label %40, !dbg !202

36:
  br label %37, !dbg !204

37:
  %38 = load i32, ptr %4, align 4, !dbg !205
  %39 = add nsw i32 %38, 1, !dbg !205
  store i32 %39, ptr %4, align 4, !dbg !205
  br label %5, !dbg !206

40:
  %41 = load i32, ptr %3, align 4, !dbg !210
  ret i32 %41, !dbg !211
}

define dso_local noundef i32 @string_ord(char*, int)(ptr noundef %0, i32 noundef %1) #2 !dbg !212 {
  %3 = alloca ptr, align 4
  %4 = alloca i32, align 4
  store ptr %0, ptr %3, align 4
    #dbg_declare(ptr %3, !215, !DIExpression(), !216)
  store i32 %1, ptr %4, align 4
    #dbg_declare(ptr %4, !217, !DIExpression(), !218)
  %5 = load ptr, ptr %3, align 4, !dbg !219
  %6 = load i32, ptr %4, align 4, !dbg !220
  %7 = getelementptr inbounds i8, ptr %5, i32 %6, !dbg !221
  %8 = load i8, ptr %7, align 1, !dbg !222
  %9 = zext i8 %8 to i32, !dbg !222
  ret i32 %9, !dbg !223
}

define dso_local noundef i32 @string_cmp(char*, char*)(ptr noundef %0, ptr noundef %1) #0 !dbg !224 {
  %3 = alloca ptr, align 4
  %4 = alloca ptr, align 4
  store ptr %0, ptr %3, align 4
    #dbg_declare(ptr %3, !227, !DIExpression(), !228)
  store ptr %1, ptr %4, align 4
    #dbg_declare(ptr %4, !229, !DIExpression(), !230)
  %5 = load ptr, ptr %3, align 4, !dbg !231
  %6 = load ptr, ptr %4, align 4, !dbg !232
  %7 = call noundef i32 @strcmp(char const*, char const*)(ptr noundef %5, ptr noundef %6), !dbg !233
  ret i32 %7, !dbg !234
}

declare dso_local noundef i32 @strcmp(char const*, char const*)(ptr noundef, ptr noundef) #1

define dso_local noundef ptr @string_cat(char*, char*)(ptr noundef %0, ptr noundef %1) #0 !dbg !235 {
  %3 = alloca ptr, align 4
  %4 = alloca ptr, align 4
  store ptr %0, ptr %3, align 4
    #dbg_declare(ptr %3, !238, !DIExpression(), !239)
  store ptr %1, ptr %4, align 4
    #dbg_declare(ptr %4, !240, !DIExpression(), !241)
  %5 = load ptr, ptr %3, align 4, !dbg !242
  %6 = load ptr, ptr %4, align 4, !dbg !243
  %7 = call noundef ptr @strcat(char*, char const*)(ptr noundef %5, ptr noundef %6), !dbg !244
  ret ptr %7, !dbg !245
}

declare dso_local noundef ptr @strcat(char*, char const*)(ptr noundef, ptr noundef) #1

define dso_local noundef ptr @ptr_array(int)(i32 noundef %0) #0 !dbg !246 {
  %2 = alloca i32, align 4
  %3 = alloca ptr, align 4
  store i32 %0, ptr %2, align 4
    #dbg_declare(ptr %2, !250, !DIExpression(), !251)
    #dbg_declare(ptr %3, !252, !DIExpression(), !253)
  %4 = load i32, ptr %2, align 4, !dbg !254
  %5 = mul i32 %4, 4, !dbg !255
  %6 = add i32 %5, 4, !dbg !256
  %7 = call noundef ptr @malloc(unsigned long)(i32 noundef %6), !dbg !257
  store ptr %7, ptr %3, align 4, !dbg !253
  %8 = load i32, ptr %2, align 4, !dbg !258
  %9 = load ptr, ptr %3, align 4, !dbg !259
  store i32 %8, ptr %9, align 4, !dbg !260
  %10 = load ptr, ptr %3, align 4, !dbg !261
  %11 = getelementptr inbounds i32, ptr %10, i32 1, !dbg !262
  ret ptr %11, !dbg !263
}

define dso_local noundef ptr @int_array(int)(i32 noundef %0) #0 !dbg !264 {
  %2 = alloca i32, align 4
  %3 = alloca ptr, align 4
  store i32 %0, ptr %2, align 4
    #dbg_declare(ptr %2, !267, !DIExpression(), !268)
    #dbg_declare(ptr %3, !269, !DIExpression(), !270)
  %4 = load i32, ptr %2, align 4, !dbg !271
  %5 = add nsw i32 %4, 1, !dbg !272
  %6 = mul i32 %5, 4, !dbg !273
  %7 = call noundef ptr @malloc(unsigned long)(i32 noundef %6), !dbg !274
  store ptr %7, ptr %3, align 4, !dbg !270
  %8 = load i32, ptr %2, align 4, !dbg !275
  %9 = load ptr, ptr %3, align 4, !dbg !276
  store i32 %8, ptr %9, align 4, !dbg !277
  %10 = load ptr, ptr %3, align 4, !dbg !278
  %11 = getelementptr inbounds i32, ptr %10, i32 1, !dbg !279
  ret ptr %11, !dbg !280
}

define dso_local noundef i32 @array_size(void*)(ptr noundef %0) #2 !dbg !281 {
  %2 = alloca ptr, align 4
  store ptr %0, ptr %2, align 4
    #dbg_declare(ptr %2, !284, !DIExpression(), !285)
  %3 = load ptr, ptr %2, align 4, !dbg !286
  %4 = getelementptr inbounds i32, ptr %3, i32 -1, !dbg !287
  %5 = load i32, ptr %4, align 4, !dbg !288
  ret i32 %5, !dbg !289
}

define dso_local noundef ptr @string_copy(char*)(ptr noundef %0) #0 !dbg !290 {
  %2 = alloca ptr, align 4
  %3 = alloca ptr, align 4
  store ptr %0, ptr %2, align 4
    #dbg_declare(ptr %2, !293, !DIExpression(), !294)
    #dbg_declare(ptr %3, !295, !DIExpression(), !296)
  %4 = load ptr, ptr %2, align 4, !dbg !297
  %5 = call noundef i32 @strlen(char const*)(ptr noundef %4), !dbg !298
  %6 = add i32 %5, 1, !dbg !299
  %7 = mul i32 %6, 1, !dbg !300
  %8 = call noundef ptr @malloc(unsigned long)(i32 noundef %7), !dbg !301
  store ptr %8, ptr %3, align 4, !dbg !296
  %9 = load ptr, ptr %3, align 4, !dbg !302
  %10 = load ptr, ptr %2, align 4, !dbg !303
  %11 = call noundef ptr @strcpy(char*, char const*)(ptr noundef %9, ptr noundef %10), !dbg !304
  %12 = load ptr, ptr %3, align 4, !dbg !305
  ret ptr %12, !dbg !306
}

attributes #0 = { mustprogress noinline optnone "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+d,+f,+m,+relax,+zicsr,+zifencei,+zmmul,-b,-e,-experimental-smmpm,-experimental-smnpm,-experimental-ssnpm,-experimental-sspm,-experimental-ssqosid,-experimental-supm,-experimental-zacas,-experimental-zalasr,-experimental-zicfilp,-experimental-zicfiss,-h,-shcounterenw,-shgatpa,-shtvala,-shvsatpa,-shvstvala,-shvstvecd,-smaia,-smcdeleg,-smcsrind,-smepmp,-smstateen,-ssaia,-ssccfg,-ssccptr,-sscofpmf,-sscounterenw,-sscsrind,-ssstateen,-ssstrict,-sstc,-sstvala,-sstvecd,-ssu64xl,-svade,-svadu,-svbare,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfcease,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xsifivecdiscarddlone,-xsifivecflushdlone,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-xwchc,-za128rs,-za64rs,-zaamo,-zabha,-zalrsc,-zama16b,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmop,-zcmp,-zcmt,-zdinx,-zfa,-zfbfmin,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zihintntl,-zihintpause,-zihpm,-zimop,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-ztso,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfbfmin,-zvfbfwma,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #1 = { "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+d,+f,+m,+relax,+zicsr,+zifencei,+zmmul,-b,-e,-experimental-smmpm,-experimental-smnpm,-experimental-ssnpm,-experimental-sspm,-experimental-ssqosid,-experimental-supm,-experimental-zacas,-experimental-zalasr,-experimental-zicfilp,-experimental-zicfiss,-h,-shcounterenw,-shgatpa,-shtvala,-shvsatpa,-shvstvala,-shvstvecd,-smaia,-smcdeleg,-smcsrind,-smepmp,-smstateen,-ssaia,-ssccfg,-ssccptr,-sscofpmf,-sscounterenw,-sscsrind,-ssstateen,-ssstrict,-sstc,-sstvala,-sstvecd,-ssu64xl,-svade,-svadu,-svbare,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfcease,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xsifivecdiscarddlone,-xsifivecflushdlone,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-xwchc,-za128rs,-za64rs,-zaamo,-zabha,-zalrsc,-zama16b,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmop,-zcmp,-zcmt,-zdinx,-zfa,-zfbfmin,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zihintntl,-zihintpause,-zihpm,-zimop,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-ztso,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfbfmin,-zvfbfwma,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #2 = { mustprogress noinline nounwind optnone "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+d,+f,+m,+relax,+zicsr,+zifencei,+zmmul,-b,-e,-experimental-smmpm,-experimental-smnpm,-experimental-ssnpm,-experimental-sspm,-experimental-ssqosid,-experimental-supm,-experimental-zacas,-experimental-zalasr,-experimental-zicfilp,-experimental-zicfiss,-h,-shcounterenw,-shgatpa,-shtvala,-shvsatpa,-shvstvala,-shvstvecd,-smaia,-smcdeleg,-smcsrind,-smepmp,-smstateen,-ssaia,-ssccfg,-ssccptr,-sscofpmf,-sscounterenw,-sscsrind,-ssstateen,-ssstrict,-sstc,-sstvala,-sstvecd,-ssu64xl,-svade,-svadu,-svbare,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfcease,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xsifivecdiscarddlone,-xsifivecflushdlone,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-xwchc,-za128rs,-za64rs,-zaamo,-zabha,-zalrsc,-zama16b,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmop,-zcmp,-zcmt,-zdinx,-zfa,-zfbfmin,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zihintntl,-zihintpause,-zihpm,-zimop,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-ztso,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfbfmin,-zvfbfwma,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
;According to the manual, this part is generated by Clang, written with C.