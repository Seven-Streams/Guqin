@.str = private unnamed_addr constant [3 x i8] c"%s\00", align 1, !dbg !0
@.str.1 = private unnamed_addr constant [2 x i8] c"\0A\00", align 1, !dbg !8
@.str.2 = private unnamed_addr constant [3 x i8] c"%d\00", align 1, !dbg !13
@.str.3 = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1, !dbg !15

define dso_local void @println(char const*)(ptr noundef %str) #0 !dbg !36 {
entry:
  %str.addr = alloca ptr, align 4
  store ptr %str, ptr %str.addr, align 4
    #dbg_declare(ptr %str.addr, !41, !DIExpression(), !42)
  %0 = load ptr, ptr %str.addr, align 4, !dbg !43
  %call = call noundef i32 @puts(char const*)(ptr noundef %0), !dbg !44
  ret void, !dbg !45
}

declare dso_local noundef i32 @puts(char const*)(ptr noundef) #1

define dso_local void @print(char const*)(ptr noundef %str) #0 !dbg !46 {
entry:
  %str.addr = alloca ptr, align 4
  store ptr %str, ptr %str.addr, align 4
    #dbg_declare(ptr %str.addr, !47, !DIExpression(), !48)
  %0 = load ptr, ptr %str.addr, align 4, !dbg !49
  %call = call noundef i32 (ptr, ...) @printf(char const*, ...)(ptr noundef @.str, ptr noundef %0), !dbg !50
  %call1 = call noundef i32 (ptr, ...) @printf(char const*, ...)(ptr noundef @.str.1), !dbg !51
  ret void, !dbg !52
}

declare dso_local noundef i32 @printf(char const*, ...)(ptr noundef, ...) #1

define dso_local void @printInt(int)(i32 noundef %x) #0 !dbg !53 {
entry:
  %x.addr = alloca i32, align 4
  store i32 %x, ptr %x.addr, align 4
    #dbg_declare(ptr %x.addr, !56, !DIExpression(), !57)
  %0 = load i32, ptr %x.addr, align 4, !dbg !58
  %call = call noundef i32 (ptr, ...) @printf(char const*, ...)(ptr noundef @.str.2, i32 noundef %0), !dbg !59
  ret void, !dbg !60
}

define dso_local void @printIntln(int)(i32 noundef %x) #0 !dbg !61 {
entry:
  %x.addr = alloca i32, align 4
  store i32 %x, ptr %x.addr, align 4
    #dbg_declare(ptr %x.addr, !62, !DIExpression(), !63)
  %0 = load i32, ptr %x.addr, align 4, !dbg !64
  %call = call noundef i32 (ptr, ...) @printf(char const*, ...)(ptr noundef @.str.3, i32 noundef %0), !dbg !65
  ret void, !dbg !66
}

define dso_local noundef ptr @toString(int)(i32 noundef %x) #0 !dbg !67 {
entry:
  %x.addr = alloca i32, align 4
  %buffer = alloca ptr, align 4
  %output = alloca ptr, align 4
  store i32 %x, ptr %x.addr, align 4
    #dbg_declare(ptr %x.addr, !70, !DIExpression(), !71)
    #dbg_declare(ptr %buffer, !72, !DIExpression(), !73)
  %call = call noundef ptr @malloc(unsigned long)(i32 noundef 15), !dbg !74
  store ptr %call, ptr %buffer, align 4, !dbg !73
  %0 = load ptr, ptr %buffer, align 4, !dbg !75
  %1 = load i32, ptr %x.addr, align 4, !dbg !76
  %call1 = call noundef i32 (ptr, ptr, ...) @sprintf(char*, char const*, ...)(ptr noundef %0, ptr noundef @.str.2, i32 noundef %1), !dbg !77
    #dbg_declare(ptr %output, !78, !DIExpression(), !79)
  %2 = load ptr, ptr %buffer, align 4, !dbg !80
  %call2 = call noundef i32 @strlen(char const*)(ptr noundef %2), !dbg !81
  %add = add i32 %call2, 1, !dbg !82
  %call3 = call noundef ptr @malloc(unsigned long)(i32 noundef %add), !dbg !83
  store ptr %call3, ptr %output, align 4, !dbg !79
  %3 = load ptr, ptr %output, align 4, !dbg !84
  %4 = load ptr, ptr %buffer, align 4, !dbg !85
  %call4 = call noundef ptr @strcpy(char*, char const*)(ptr noundef %3, ptr noundef %4), !dbg !86
  %5 = load ptr, ptr %buffer, align 4, !dbg !87
  call void @free(void*)(ptr noundef %5), !dbg !88
  %6 = load ptr, ptr %output, align 4, !dbg !89
  ret ptr %6, !dbg !90
}

declare dso_local noundef ptr @malloc(unsigned long)(i32 noundef) #1

declare dso_local noundef i32 @sprintf(char*, char const*, ...)(ptr noundef, ptr noundef, ...) #1

declare dso_local noundef i32 @strlen(char const*)(ptr noundef) #1

declare dso_local noundef ptr @strcpy(char*, char const*)(ptr noundef, ptr noundef) #1

declare dso_local void @free(void*)(ptr noundef) #1

define dso_local noundef i32 @getInt()() #0 !dbg !91 {
entry:
  %x = alloca i32, align 4
    #dbg_declare(ptr %x, !94, !DIExpression(), !95)
  %call = call noundef i32 (ptr, ...) @scanf(char const*, ...)(ptr noundef @.str.2, ptr noundef %x), !dbg !96
  %0 = load i32, ptr %x, align 4, !dbg !97
  ret i32 %0, !dbg !98
}

declare dso_local noundef i32 @scanf(char const*, ...)(ptr noundef, ...) #1

define dso_local noundef ptr @getString()() #0 !dbg !99 {
entry:
  %buffer = alloca ptr, align 4
  %output = alloca ptr, align 4
    #dbg_declare(ptr %buffer, !102, !DIExpression(), !103)
  %call = call noundef ptr @malloc(unsigned long)(i32 noundef 4096), !dbg !104
  store ptr %call, ptr %buffer, align 4, !dbg !103
  %0 = load ptr, ptr %buffer, align 4, !dbg !105
  %call1 = call noundef i32 (ptr, ...) @scanf(char const*, ...)(ptr noundef @.str, ptr noundef %0), !dbg !106
    #dbg_declare(ptr %output, !107, !DIExpression(), !108)
  %1 = load ptr, ptr %buffer, align 4, !dbg !109
  %call2 = call noundef i32 @strlen(char const*)(ptr noundef %1), !dbg !110
  %add = add i32 %call2, 1, !dbg !111
  %call3 = call noundef ptr @malloc(unsigned long)(i32 noundef %add), !dbg !112
  store ptr %call3, ptr %output, align 4, !dbg !108
  %2 = load ptr, ptr %output, align 4, !dbg !113
  %3 = load ptr, ptr %buffer, align 4, !dbg !114
  %call4 = call noundef ptr @strcpy(char*, char const*)(ptr noundef %2, ptr noundef %3), !dbg !115
  %4 = load ptr, ptr %buffer, align 4, !dbg !116
  call void @free(void*)(ptr noundef %4), !dbg !117
  %5 = load ptr, ptr %output, align 4, !dbg !118
  ret ptr %5, !dbg !119
}

define dso_local noundef i32 @string_length(char*)(ptr noundef %str) #0 !dbg !120 {
entry:
  %str.addr = alloca ptr, align 4
  store ptr %str, ptr %str.addr, align 4
    #dbg_declare(ptr %str.addr, !123, !DIExpression(), !124)
  %0 = load ptr, ptr %str.addr, align 4, !dbg !125
  %call = call noundef i32 @strlen(char const*)(ptr noundef %0), !dbg !126
  ret i32 %call, !dbg !127
}

define dso_local noundef ptr @string_substring(char*, int, int)(ptr noundef %str, i32 noundef %left, i32 noundef %right) #0 !dbg !128 {
entry:
  %str.addr = alloca ptr, align 4
  %left.addr = alloca i32, align 4
  %right.addr = alloca i32, align 4
  %res = alloca ptr, align 4
  %output = alloca ptr, align 4
  store ptr %str, ptr %str.addr, align 4
    #dbg_declare(ptr %str.addr, !131, !DIExpression(), !132)
  store i32 %left, ptr %left.addr, align 4
    #dbg_declare(ptr %left.addr, !133, !DIExpression(), !134)
  store i32 %right, ptr %right.addr, align 4
    #dbg_declare(ptr %right.addr, !135, !DIExpression(), !136)
    #dbg_declare(ptr %res, !137, !DIExpression(), !138)
  %call = call noundef ptr @malloc(unsigned long)(i32 noundef 5), !dbg !139
  store ptr %call, ptr %res, align 4, !dbg !138
  %0 = load ptr, ptr %res, align 4, !dbg !140
  %1 = load ptr, ptr %str.addr, align 4, !dbg !141
  %2 = load i32, ptr %left.addr, align 4, !dbg !142
  %add.ptr = getelementptr inbounds i8, ptr %1, i32 %2, !dbg !143
  %call1 = call noundef ptr @strcpy(char*, char const*)(ptr noundef %0, ptr noundef %add.ptr), !dbg !144
  %3 = load ptr, ptr %res, align 4, !dbg !145
  %4 = load i32, ptr %right.addr, align 4, !dbg !146
  %5 = load i32, ptr %left.addr, align 4, !dbg !147
  %sub = sub nsw i32 %4, %5, !dbg !148
  %add.ptr2 = getelementptr inbounds i8, ptr %3, i32 %sub, !dbg !149
  store i8 0, ptr %add.ptr2, align 1, !dbg !150
    #dbg_declare(ptr %output, !151, !DIExpression(), !152)
  %6 = load ptr, ptr %res, align 4, !dbg !153
  %call3 = call noundef i32 @strlen(char const*)(ptr noundef %6), !dbg !154
  %add = add i32 %call3, 1, !dbg !155
  %call4 = call noundef ptr @malloc(unsigned long)(i32 noundef %add), !dbg !156
  store ptr %call4, ptr %output, align 4, !dbg !152
  %7 = load ptr, ptr %output, align 4, !dbg !157
  %8 = load ptr, ptr %res, align 4, !dbg !158
  %call5 = call noundef ptr @strcpy(char*, char const*)(ptr noundef %7, ptr noundef %8), !dbg !159
  %9 = load ptr, ptr %res, align 4, !dbg !160
  call void @free(void*)(ptr noundef %9), !dbg !161
  %10 = load ptr, ptr %output, align 4, !dbg !162
  ret ptr %10, !dbg !163
}

define dso_local noundef i32 @string_parseInt(char*)(ptr noundef %str) #0 !dbg !164 {
entry:
  %str.addr = alloca ptr, align 4
  %ans = alloca i32, align 4
  %i = alloca i32, align 4
  store ptr %str, ptr %str.addr, align 4
    #dbg_declare(ptr %str.addr, !165, !DIExpression(), !166)
    #dbg_declare(ptr %ans, !167, !DIExpression(), !168)
  store i32 0, ptr %ans, align 4, !dbg !168
    #dbg_declare(ptr %i, !169, !DIExpression(), !171)
  store i32 0, ptr %i, align 4, !dbg !171
  br label %for.cond, !dbg !172

for.cond:
  %0 = load i32, ptr %i, align 4, !dbg !173
  %1 = load ptr, ptr %str.addr, align 4, !dbg !175
  %call = call noundef i32 @strlen(char const*)(ptr noundef %1), !dbg !176
  %cmp = icmp ult i32 %0, %call, !dbg !177
  br i1 %cmp, label %for.body, label %for.end, !dbg !178

for.body:
  %2 = load ptr, ptr %str.addr, align 4, !dbg !179
  %3 = load i32, ptr %i, align 4, !dbg !182
  %add.ptr = getelementptr inbounds i8, ptr %2, i32 %3, !dbg !183
  %4 = load i8, ptr %add.ptr, align 1, !dbg !184
  %conv = zext i8 %4 to i32, !dbg !184
  %cmp1 = icmp sge i32 %conv, 48, !dbg !185
  br i1 %cmp1, label %land.lhs.true, label %if.else, !dbg !186

land.lhs.true:
  %5 = load ptr, ptr %str.addr, align 4, !dbg !187
  %6 = load i32, ptr %i, align 4, !dbg !188
  %add.ptr2 = getelementptr inbounds i8, ptr %5, i32 %6, !dbg !189
  %7 = load i8, ptr %add.ptr2, align 1, !dbg !190
  %conv3 = zext i8 %7 to i32, !dbg !190
  %cmp4 = icmp sle i32 %conv3, 57, !dbg !191
  br i1 %cmp4, label %if.then, label %if.else, !dbg !192

if.then:
  %8 = load i32, ptr %ans, align 4, !dbg !193
  %mul = mul nsw i32 %8, 10, !dbg !193
  store i32 %mul, ptr %ans, align 4, !dbg !193
  %9 = load ptr, ptr %str.addr, align 4, !dbg !195
  %10 = load i32, ptr %i, align 4, !dbg !196
  %add.ptr5 = getelementptr inbounds i8, ptr %9, i32 %10, !dbg !197
  %11 = load i8, ptr %add.ptr5, align 1, !dbg !198
  %conv6 = zext i8 %11 to i32, !dbg !198
  %sub = sub nsw i32 %conv6, 48, !dbg !199
  %12 = load i32, ptr %ans, align 4, !dbg !200
  %add = add nsw i32 %12, %sub, !dbg !200
  store i32 %add, ptr %ans, align 4, !dbg !200
  br label %if.end, !dbg !201

if.else:
  br label %for.end, !dbg !202

if.end:
  br label %for.inc, !dbg !204

for.inc:
  %13 = load i32, ptr %i, align 4, !dbg !205
  %inc = add nsw i32 %13, 1, !dbg !205
  store i32 %inc, ptr %i, align 4, !dbg !205
  br label %for.cond, !dbg !206

for.end:
  %14 = load i32, ptr %ans, align 4, !dbg !210
  ret i32 %14, !dbg !211
}

define dso_local noundef i32 @string_ord(char*, int)(ptr noundef %str, i32 noundef %num) #2 !dbg !212 {
entry:
  %str.addr = alloca ptr, align 4
  %num.addr = alloca i32, align 4
  store ptr %str, ptr %str.addr, align 4
    #dbg_declare(ptr %str.addr, !215, !DIExpression(), !216)
  store i32 %num, ptr %num.addr, align 4
    #dbg_declare(ptr %num.addr, !217, !DIExpression(), !218)
  %0 = load ptr, ptr %str.addr, align 4, !dbg !219
  %1 = load i32, ptr %num.addr, align 4, !dbg !220
  %add.ptr = getelementptr inbounds i8, ptr %0, i32 %1, !dbg !221
  %2 = load i8, ptr %add.ptr, align 1, !dbg !222
  %conv = zext i8 %2 to i32, !dbg !222
  ret i32 %conv, !dbg !223
}

define dso_local noundef i32 @string_cmp(char*, char*)(ptr noundef %str1, ptr noundef %str2) #0 !dbg !224 {
entry:
  %str1.addr = alloca ptr, align 4
  %str2.addr = alloca ptr, align 4
  store ptr %str1, ptr %str1.addr, align 4
    #dbg_declare(ptr %str1.addr, !227, !DIExpression(), !228)
  store ptr %str2, ptr %str2.addr, align 4
    #dbg_declare(ptr %str2.addr, !229, !DIExpression(), !230)
  %0 = load ptr, ptr %str1.addr, align 4, !dbg !231
  %1 = load ptr, ptr %str2.addr, align 4, !dbg !232
  %call = call noundef i32 @strcmp(char const*, char const*)(ptr noundef %0, ptr noundef %1), !dbg !233
  ret i32 %call, !dbg !234
}

declare dso_local noundef i32 @strcmp(char const*, char const*)(ptr noundef, ptr noundef) #1

define dso_local noundef ptr @string_cat(char*, char*)(ptr noundef %str1, ptr noundef %str2) #0 !dbg !235 {
entry:
  %str1.addr = alloca ptr, align 4
  %str2.addr = alloca ptr, align 4
  store ptr %str1, ptr %str1.addr, align 4
    #dbg_declare(ptr %str1.addr, !238, !DIExpression(), !239)
  store ptr %str2, ptr %str2.addr, align 4
    #dbg_declare(ptr %str2.addr, !240, !DIExpression(), !241)
  %0 = load ptr, ptr %str1.addr, align 4, !dbg !242
  %1 = load ptr, ptr %str2.addr, align 4, !dbg !243
  %call = call noundef ptr @strcat(char*, char const*)(ptr noundef %0, ptr noundef %1), !dbg !244
  ret ptr %call, !dbg !245
}

declare dso_local noundef ptr @strcat(char*, char const*)(ptr noundef, ptr noundef) #1

define dso_local noundef ptr @ptr_array(int)(i32 noundef %number) #0 !dbg !246 {
entry:
  %number.addr = alloca i32, align 4
  %res = alloca ptr, align 4
  store i32 %number, ptr %number.addr, align 4
    #dbg_declare(ptr %number.addr, !250, !DIExpression(), !251)
    #dbg_declare(ptr %res, !252, !DIExpression(), !253)
  %0 = load i32, ptr %number.addr, align 4, !dbg !254
  %mul = mul i32 %0, 4, !dbg !255
  %add = add i32 %mul, 4, !dbg !256
  %call = call noundef ptr @malloc(unsigned long)(i32 noundef %add), !dbg !257
  store ptr %call, ptr %res, align 4, !dbg !253
  %1 = load i32, ptr %number.addr, align 4, !dbg !258
  %2 = load ptr, ptr %res, align 4, !dbg !259
  store i32 %1, ptr %2, align 4, !dbg !260
  %3 = load ptr, ptr %res, align 4, !dbg !261
  %add.ptr = getelementptr inbounds i32, ptr %3, i32 1, !dbg !262
  ret ptr %add.ptr, !dbg !263
}

define dso_local noundef ptr @int_array(int)(i32 noundef %number) #0 !dbg !264 {
entry:
  %number.addr = alloca i32, align 4
  %res = alloca ptr, align 4
  store i32 %number, ptr %number.addr, align 4
    #dbg_declare(ptr %number.addr, !267, !DIExpression(), !268)
    #dbg_declare(ptr %res, !269, !DIExpression(), !270)
  %0 = load i32, ptr %number.addr, align 4, !dbg !271
  %add = add nsw i32 %0, 1, !dbg !272
  %mul = mul i32 %add, 4, !dbg !273
  %call = call noundef ptr @malloc(unsigned long)(i32 noundef %mul), !dbg !274
  store ptr %call, ptr %res, align 4, !dbg !270
  %1 = load i32, ptr %number.addr, align 4, !dbg !275
  %2 = load ptr, ptr %res, align 4, !dbg !276
  store i32 %1, ptr %2, align 4, !dbg !277
  %3 = load ptr, ptr %res, align 4, !dbg !278
  %add.ptr = getelementptr inbounds i32, ptr %3, i32 1, !dbg !279
  ret ptr %add.ptr, !dbg !280
}

define dso_local noundef i32 @array_size(void*)(ptr noundef %ptr) #2 !dbg !281 {
entry:
  %ptr.addr = alloca ptr, align 4
  store ptr %ptr, ptr %ptr.addr, align 4
    #dbg_declare(ptr %ptr.addr, !284, !DIExpression(), !285)
  %0 = load ptr, ptr %ptr.addr, align 4, !dbg !286
  %add.ptr = getelementptr inbounds i32, ptr %0, i32 -1, !dbg !287
  %1 = load i32, ptr %add.ptr, align 4, !dbg !288
  ret i32 %1, !dbg !289
}

define dso_local noundef ptr @string_copy(char*)(ptr noundef %str) #0 !dbg !290 {
entry:
  %str.addr = alloca ptr, align 4
  %copy = alloca ptr, align 4
  store ptr %str, ptr %str.addr, align 4
    #dbg_declare(ptr %str.addr, !293, !DIExpression(), !294)
    #dbg_declare(ptr %copy, !295, !DIExpression(), !296)
  %0 = load ptr, ptr %str.addr, align 4, !dbg !297
  %call = call noundef i32 @strlen(char const*)(ptr noundef %0), !dbg !298
  %add = add i32 %call, 1, !dbg !299
  %mul = mul i32 %add, 1, !dbg !300
  %call1 = call noundef ptr @malloc(unsigned long)(i32 noundef %mul), !dbg !301
  store ptr %call1, ptr %copy, align 4, !dbg !296
  %1 = load ptr, ptr %copy, align 4, !dbg !302
  %2 = load ptr, ptr %str.addr, align 4, !dbg !303
  %call2 = call noundef ptr @strcpy(char*, char const*)(ptr noundef %1, ptr noundef %2), !dbg !304
  %3 = load ptr, ptr %copy, align 4, !dbg !305
  ret ptr %3, !dbg !306
}

attributes #0 = { mustprogress noinline optnone "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+d,+f,+m,+relax,+zicsr,+zifencei,+zmmul,-b,-e,-experimental-smmpm,-experimental-smnpm,-experimental-ssnpm,-experimental-sspm,-experimental-ssqosid,-experimental-supm,-experimental-zacas,-experimental-zalasr,-experimental-zicfilp,-experimental-zicfiss,-h,-shcounterenw,-shgatpa,-shtvala,-shvsatpa,-shvstvala,-shvstvecd,-smaia,-smcdeleg,-smcsrind,-smepmp,-smstateen,-ssaia,-ssccfg,-ssccptr,-sscofpmf,-sscounterenw,-sscsrind,-ssstateen,-ssstrict,-sstc,-sstvala,-sstvecd,-ssu64xl,-svade,-svadu,-svbare,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfcease,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xsifivecdiscarddlone,-xsifivecflushdlone,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-xwchc,-za128rs,-za64rs,-zaamo,-zabha,-zalrsc,-zama16b,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmop,-zcmp,-zcmt,-zdinx,-zfa,-zfbfmin,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zihintntl,-zihintpause,-zihpm,-zimop,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-ztso,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfbfmin,-zvfbfwma,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #1 = { "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+d,+f,+m,+relax,+zicsr,+zifencei,+zmmul,-b,-e,-experimental-smmpm,-experimental-smnpm,-experimental-ssnpm,-experimental-sspm,-experimental-ssqosid,-experimental-supm,-experimental-zacas,-experimental-zalasr,-experimental-zicfilp,-experimental-zicfiss,-h,-shcounterenw,-shgatpa,-shtvala,-shvsatpa,-shvstvala,-shvstvecd,-smaia,-smcdeleg,-smcsrind,-smepmp,-smstateen,-ssaia,-ssccfg,-ssccptr,-sscofpmf,-sscounterenw,-sscsrind,-ssstateen,-ssstrict,-sstc,-sstvala,-sstvecd,-ssu64xl,-svade,-svadu,-svbare,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfcease,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xsifivecdiscarddlone,-xsifivecflushdlone,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-xwchc,-za128rs,-za64rs,-zaamo,-zabha,-zalrsc,-zama16b,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmop,-zcmp,-zcmt,-zdinx,-zfa,-zfbfmin,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zihintntl,-zihintpause,-zihpm,-zimop,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-ztso,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfbfmin,-zvfbfwma,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #2 = { mustprogress noinline nounwind optnone "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+d,+f,+m,+relax,+zicsr,+zifencei,+zmmul,-b,-e,-experimental-smmpm,-experimental-smnpm,-experimental-ssnpm,-experimental-sspm,-experimental-ssqosid,-experimental-supm,-experimental-zacas,-experimental-zalasr,-experimental-zicfilp,-experimental-zicfiss,-h,-shcounterenw,-shgatpa,-shtvala,-shvsatpa,-shvstvala,-shvstvecd,-smaia,-smcdeleg,-smcsrind,-smepmp,-smstateen,-ssaia,-ssccfg,-ssccptr,-sscofpmf,-sscounterenw,-sscsrind,-ssstateen,-ssstrict,-sstc,-sstvala,-sstvecd,-ssu64xl,-svade,-svadu,-svbare,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfcease,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xsifivecdiscarddlone,-xsifivecflushdlone,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-xwchc,-za128rs,-za64rs,-zaamo,-zabha,-zalrsc,-zama16b,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmop,-zcmp,-zcmt,-zdinx,-zfa,-zfbfmin,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zihintntl,-zihintpause,-zihpm,-zimop,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-ztso,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfbfmin,-zvfbfwma,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }

attributes #0 = { mustprogress noinline optnone "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+d,+f,+m,+relax,+zicsr,+zifencei,+zmmul,-b,-e,-experimental-smmpm,-experimental-smnpm,-experimental-ssnpm,-experimental-sspm,-experimental-ssqosid,-experimental-supm,-experimental-zacas,-experimental-zalasr,-experimental-zicfilp,-experimental-zicfiss,-h,-shcounterenw,-shgatpa,-shtvala,-shvsatpa,-shvstvala,-shvstvecd,-smaia,-smcdeleg,-smcsrind,-smepmp,-smstateen,-ssaia,-ssccfg,-ssccptr,-sscofpmf,-sscounterenw,-sscsrind,-ssstateen,-ssstrict,-sstc,-sstvala,-sstvecd,-ssu64xl,-svade,-svadu,-svbare,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfcease,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xsifivecdiscarddlone,-xsifivecflushdlone,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-xwchc,-za128rs,-za64rs,-zaamo,-zabha,-zalrsc,-zama16b,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmop,-zcmp,-zcmt,-zdinx,-zfa,-zfbfmin,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zihintntl,-zihintpause,-zihpm,-zimop,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-ztso,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfbfmin,-zvfbfwma,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #1 = { "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+d,+f,+m,+relax,+zicsr,+zifencei,+zmmul,-b,-e,-experimental-smmpm,-experimental-smnpm,-experimental-ssnpm,-experimental-sspm,-experimental-ssqosid,-experimental-supm,-experimental-zacas,-experimental-zalasr,-experimental-zicfilp,-experimental-zicfiss,-h,-shcounterenw,-shgatpa,-shtvala,-shvsatpa,-shvstvala,-shvstvecd,-smaia,-smcdeleg,-smcsrind,-smepmp,-smstateen,-ssaia,-ssccfg,-ssccptr,-sscofpmf,-sscounterenw,-sscsrind,-ssstateen,-ssstrict,-sstc,-sstvala,-sstvecd,-ssu64xl,-svade,-svadu,-svbare,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfcease,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xsifivecdiscarddlone,-xsifivecflushdlone,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-xwchc,-za128rs,-za64rs,-zaamo,-zabha,-zalrsc,-zama16b,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmop,-zcmp,-zcmt,-zdinx,-zfa,-zfbfmin,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zihintntl,-zihintpause,-zihpm,-zimop,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-ztso,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfbfmin,-zvfbfwma,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #2 = { mustprogress noinline nounwind optnone "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+d,+f,+m,+relax,+zicsr,+zifencei,+zmmul,-b,-e,-experimental-smmpm,-experimental-smnpm,-experimental-ssnpm,-experimental-sspm,-experimental-ssqosid,-experimental-supm,-experimental-zacas,-experimental-zalasr,-experimental-zicfilp,-experimental-zicfiss,-h,-shcounterenw,-shgatpa,-shtvala,-shvsatpa,-shvstvala,-shvstvecd,-smaia,-smcdeleg,-smcsrind,-smepmp,-smstateen,-ssaia,-ssccfg,-ssccptr,-sscofpmf,-sscounterenw,-sscsrind,-ssstateen,-ssstrict,-sstc,-sstvala,-sstvecd,-ssu64xl,-svade,-svadu,-svbare,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfcease,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xsifivecdiscarddlone,-xsifivecflushdlone,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-xwchc,-za128rs,-za64rs,-zaamo,-zabha,-zalrsc,-zama16b,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmop,-zcmp,-zcmt,-zdinx,-zfa,-zfbfmin,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zihintntl,-zihintpause,-zihpm,-zimop,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-ztso,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfbfmin,-zvfbfwma,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
;According to the manual, this part is generated by Clang, written with C.