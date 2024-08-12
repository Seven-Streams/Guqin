	.text
	.attribute	4, 16
	.attribute	5, "rv32i2p1_m2p0_a2p1_c2p0"
	.file	"Inner.c"
	.globl	println                         # -- Begin function println
	.p2align	1
	.type	println,@function
println:                                # @println
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	addi	s0, sp, 16
	sw	a0, -12(s0)
	lw	a0, -12(s0)
	call	puts
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end0:
	.size	println, .Lfunc_end0-println
                                        # -- End function
	.globl	print                           # -- Begin function print
	.p2align	1
	.type	print,@function
print:                                  # @print
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	addi	s0, sp, 16
	sw	a0, -12(s0)
	lw	a1, -12(s0)
	lui	a0, %hi(.L.str)
	addi	a0, a0, %lo(.L.str)
	call	printf
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end1:
	.size	print, .Lfunc_end1-print
                                        # -- End function
	.globl	printInt                        # -- Begin function printInt
	.p2align	1
	.type	printInt,@function
printInt:                               # @printInt
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	addi	s0, sp, 16
	sw	a0, -12(s0)
	lw	a1, -12(s0)
	lui	a0, %hi(.L.str.1)
	addi	a0, a0, %lo(.L.str.1)
	call	printf
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end2:
	.size	printInt, .Lfunc_end2-printInt
                                        # -- End function
	.globl	printIntln                      # -- Begin function printIntln
	.p2align	1
	.type	printIntln,@function
printIntln:                             # @printIntln
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	addi	s0, sp, 16
	sw	a0, -12(s0)
	lw	a1, -12(s0)
	lui	a0, %hi(.L.str.2)
	addi	a0, a0, %lo(.L.str.2)
	call	printf
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end3:
	.size	printIntln, .Lfunc_end3-printIntln
                                        # -- End function
	.globl	toString                        # -- Begin function toString
	.p2align	1
	.type	toString,@function
toString:                               # @toString
# %bb.0:
	addi	sp, sp, -32
	sw	ra, 28(sp)                      # 4-byte Folded Spill
	sw	s0, 24(sp)                      # 4-byte Folded Spill
	addi	s0, sp, 32
	sw	a0, -12(s0)
	li	a0, 15
	call	malloc
	sw	a0, -16(s0)
	lw	a0, -16(s0)
	lw	a2, -12(s0)
	lui	a1, %hi(.L.str.1)
	addi	a1, a1, %lo(.L.str.1)
	call	sprintf
	lw	a0, -16(s0)
	call	strlen
	addi	a0, a0, 1
	call	malloc
	sw	a0, -20(s0)
	lw	a0, -20(s0)
	lw	a1, -16(s0)
	call	strcpy
	lw	a0, -16(s0)
	call	free
	lw	a0, -20(s0)
	lw	ra, 28(sp)                      # 4-byte Folded Reload
	lw	s0, 24(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 32
	ret
.Lfunc_end4:
	.size	toString, .Lfunc_end4-toString
                                        # -- End function
	.globl	getInt                          # -- Begin function getInt
	.p2align	1
	.type	getInt,@function
getInt:                                 # @getInt
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	addi	s0, sp, 16
	lui	a0, %hi(.L.str.1)
	addi	a0, a0, %lo(.L.str.1)
	addi	a1, s0, -12
	call	scanf
	lw	a0, -12(s0)
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end5:
	.size	getInt, .Lfunc_end5-getInt
                                        # -- End function
	.globl	getString                       # -- Begin function getString
	.p2align	1
	.type	getString,@function
getString:                              # @getString
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	addi	s0, sp, 16
	lui	a0, 1
	call	malloc
	sw	a0, -12(s0)
	lw	a1, -12(s0)
	lui	a0, %hi(.L.str)
	addi	a0, a0, %lo(.L.str)
	call	scanf
	lw	a0, -12(s0)
	call	strlen
	addi	a0, a0, 1
	call	malloc
	sw	a0, -16(s0)
	lw	a0, -16(s0)
	lw	a1, -12(s0)
	call	strcpy
	lw	a0, -12(s0)
	call	free
	lw	a0, -16(s0)
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end6:
	.size	getString, .Lfunc_end6-getString
                                        # -- End function
	.globl	string_length                   # -- Begin function string_length
	.p2align	1
	.type	string_length,@function
string_length:                          # @string_length
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	addi	s0, sp, 16
	sw	a0, -12(s0)
	lw	a0, -12(s0)
	call	strlen
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end7:
	.size	string_length, .Lfunc_end7-string_length
                                        # -- End function
	.globl	string_substring                # -- Begin function string_substring
	.p2align	1
	.type	string_substring,@function
string_substring:                       # @string_substring
# %bb.0:
	addi	sp, sp, -32
	sw	ra, 28(sp)                      # 4-byte Folded Spill
	sw	s0, 24(sp)                      # 4-byte Folded Spill
	addi	s0, sp, 32
	sw	a0, -12(s0)
	sw	a1, -16(s0)
	sw	a2, -20(s0)
	li	a0, 5
	call	malloc
	sw	a0, -24(s0)
	lw	a0, -24(s0)
	lw	a1, -12(s0)
	lw	a2, -16(s0)
	add	a1, a1, a2
	call	strcpy
	lw	a0, -24(s0)
	lw	a1, -20(s0)
	lw	a2, -16(s0)
	sub	a1, a1, a2
	add	a1, a1, a0
	li	a0, 0
	sb	a0, 0(a1)
	lw	a0, -24(s0)
	call	strlen
	addi	a0, a0, 1
	call	malloc
	sw	a0, -28(s0)
	lw	a0, -28(s0)
	lw	a1, -24(s0)
	call	strcpy
	lw	a0, -24(s0)
	call	free
	lw	a0, -28(s0)
	lw	ra, 28(sp)                      # 4-byte Folded Reload
	lw	s0, 24(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 32
	ret
.Lfunc_end8:
	.size	string_substring, .Lfunc_end8-string_substring
                                        # -- End function
	.globl	string_parseInt                 # -- Begin function string_parseInt
	.p2align	1
	.type	string_parseInt,@function
string_parseInt:                        # @string_parseInt
# %bb.0:
	addi	sp, sp, -32
	sw	ra, 28(sp)                      # 4-byte Folded Spill
	sw	s0, 24(sp)                      # 4-byte Folded Spill
	addi	s0, sp, 32
	sw	a0, -12(s0)
	li	a0, 0
	sw	a0, -16(s0)
	sw	a0, -20(s0)
	j	.LBB9_1
.LBB9_1:                                # =>This Inner Loop Header: Depth=1
	lw	a0, -20(s0)
	sw	a0, -24(s0)                     # 4-byte Folded Spill
	lw	a0, -12(s0)
	call	strlen
	mv	a1, a0
	lw	a0, -24(s0)                     # 4-byte Folded Reload
	bgeu	a0, a1, .LBB9_8
	j	.LBB9_2
.LBB9_2:                                #   in Loop: Header=BB9_1 Depth=1
	lw	a0, -12(s0)
	lw	a1, -20(s0)
	add	a0, a0, a1
	lbu	a0, 0(a0)
	li	a1, 48
	blt	a0, a1, .LBB9_5
	j	.LBB9_3
.LBB9_3:                                #   in Loop: Header=BB9_1 Depth=1
	lw	a0, -12(s0)
	lw	a1, -20(s0)
	add	a0, a0, a1
	lbu	a1, 0(a0)
	li	a0, 57
	blt	a0, a1, .LBB9_5
	j	.LBB9_4
.LBB9_4:                                #   in Loop: Header=BB9_1 Depth=1
	lw	a0, -16(s0)
	li	a1, 10
	mul	a0, a0, a1
	sw	a0, -16(s0)
	lw	a0, -12(s0)
	lw	a1, -20(s0)
	add	a0, a0, a1
	lbu	a0, 0(a0)
	lw	a1, -16(s0)
	add	a0, a0, a1
	addi	a0, a0, -48
	sw	a0, -16(s0)
	j	.LBB9_6
.LBB9_5:
	j	.LBB9_8
.LBB9_6:                                #   in Loop: Header=BB9_1 Depth=1
	j	.LBB9_7
.LBB9_7:                                #   in Loop: Header=BB9_1 Depth=1
	lw	a0, -20(s0)
	addi	a0, a0, 1
	sw	a0, -20(s0)
	j	.LBB9_1
.LBB9_8:
	lw	a0, -16(s0)
	lw	ra, 28(sp)                      # 4-byte Folded Reload
	lw	s0, 24(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 32
	ret
.Lfunc_end9:
	.size	string_parseInt, .Lfunc_end9-string_parseInt
                                        # -- End function
	.globl	string_ord                      # -- Begin function string_ord
	.p2align	1
	.type	string_ord,@function
string_ord:                             # @string_ord
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	addi	s0, sp, 16
	sw	a0, -12(s0)
	sw	a1, -16(s0)
	lw	a0, -12(s0)
	lw	a1, -16(s0)
	add	a0, a0, a1
	lbu	a0, 0(a0)
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end10:
	.size	string_ord, .Lfunc_end10-string_ord
                                        # -- End function
	.globl	string_cmp                      # -- Begin function string_cmp
	.p2align	1
	.type	string_cmp,@function
string_cmp:                             # @string_cmp
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	addi	s0, sp, 16
	sw	a0, -12(s0)
	sw	a1, -16(s0)
	lw	a0, -12(s0)
	lw	a1, -16(s0)
	call	strcmp
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end11:
	.size	string_cmp, .Lfunc_end11-string_cmp
                                        # -- End function
	.globl	string_cat                      # -- Begin function string_cat
	.p2align	1
	.type	string_cat,@function
string_cat:                             # @string_cat
# %bb.0:
	addi	sp, sp, -32
	sw	ra, 28(sp)                      # 4-byte Folded Spill
	sw	s0, 24(sp)                      # 4-byte Folded Spill
	addi	s0, sp, 32
	sw	a0, -12(s0)
	sw	a1, -16(s0)
	lw	a0, -12(s0)
	call	string_length
	sw	a0, -24(s0)                     # 4-byte Folded Spill
	lw	a0, -16(s0)
	call	string_length
	mv	a1, a0
	lw	a0, -24(s0)                     # 4-byte Folded Reload
	add	a0, a0, a1
	addi	a0, a0, 1
	call	malloc
	sw	a0, -20(s0)
	lw	a0, -20(s0)
	lw	a1, -12(s0)
	call	strcpy
	lw	a0, -20(s0)
	lw	a1, -16(s0)
	call	strcat
	lw	a0, -20(s0)
	lw	ra, 28(sp)                      # 4-byte Folded Reload
	lw	s0, 24(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 32
	ret
.Lfunc_end12:
	.size	string_cat, .Lfunc_end12-string_cat
                                        # -- End function
	.globl	ptr_array                       # -- Begin function ptr_array
	.p2align	1
	.type	ptr_array,@function
ptr_array:                              # @ptr_array
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	addi	s0, sp, 16
	sw	a0, -12(s0)
	lw	a0, -12(s0)
	slli	a0, a0, 2
	addi	a0, a0, 4
	call	malloc
	sw	a0, -16(s0)
	lw	a0, -12(s0)
	lw	a1, -16(s0)
	sw	a0, 0(a1)
	lw	a0, -16(s0)
	addi	a0, a0, 4
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end13:
	.size	ptr_array, .Lfunc_end13-ptr_array
                                        # -- End function
	.globl	int_array                       # -- Begin function int_array
	.p2align	1
	.type	int_array,@function
int_array:                              # @int_array
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	addi	s0, sp, 16
	sw	a0, -12(s0)
	lw	a0, -12(s0)
	slli	a0, a0, 2
	addi	a0, a0, 4
	call	malloc
	sw	a0, -16(s0)
	lw	a0, -12(s0)
	lw	a1, -16(s0)
	sw	a0, 0(a1)
	lw	a0, -16(s0)
	addi	a0, a0, 4
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end14:
	.size	int_array, .Lfunc_end14-int_array
                                        # -- End function
	.globl	array_size                      # -- Begin function array_size
	.p2align	1
	.type	array_size,@function
array_size:                             # @array_size
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	addi	s0, sp, 16
	sw	a0, -12(s0)
	lw	a0, -12(s0)
	lw	a0, -4(a0)
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end15:
	.size	array_size, .Lfunc_end15-array_size
                                        # -- End function
	.globl	string_copy                     # -- Begin function string_copy
	.p2align	1
	.type	string_copy,@function
string_copy:                            # @string_copy
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	addi	s0, sp, 16
	sw	a0, -12(s0)
	lw	a0, -12(s0)
	call	strlen
	addi	a0, a0, 1
	call	malloc
	sw	a0, -16(s0)
	lw	a0, -16(s0)
	lw	a1, -12(s0)
	call	strcpy
	lw	a0, -16(s0)
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end16:
	.size	string_copy, .Lfunc_end16-string_copy
                                        # -- End function
	.globl	MyNew                           # -- Begin function MyNew
	.p2align	1
	.type	MyNew,@function
MyNew:                                  # @MyNew
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	addi	s0, sp, 16
	sw	a0, -12(s0)
	lw	a0, -12(s0)
	call	malloc
	sw	a0, -16(s0)
	lw	a0, -16(s0)
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end17:
	.size	MyNew, .Lfunc_end17-MyNew
                                        # -- End function
	.type	.L.str,@object                  # @.str
	.section	.rodata.str1.1,"aMS",@progbits,1
.L.str:
	.asciz	"%s"
	.size	.L.str, 3

	.type	.L.str.1,@object                # @.str.1
.L.str.1:
	.asciz	"%d"
	.size	.L.str.1, 3

	.type	.L.str.2,@object                # @.str.2
.L.str.2:
	.asciz	"%d\n"
	.size	.L.str.2, 4

	.ident	"Ubuntu clang version 18.1.8 (++20240731024944+3b5b5c1ec4a3-1~exp1~20240731145000.144)"
	.section	".note.GNU-stack","",@progbits
	.addrsig
	.addrsig_sym puts
	.addrsig_sym printf
	.addrsig_sym malloc
	.addrsig_sym sprintf
	.addrsig_sym strlen
	.addrsig_sym strcpy
	.addrsig_sym free
	.addrsig_sym scanf
	.addrsig_sym string_length
	.addrsig_sym strcmp
	.addrsig_sym strcat
