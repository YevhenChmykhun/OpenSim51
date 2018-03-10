package assembler.syntaxanalyzer;

// Generated from C:/Users/yevhen/IdeaProjects/8051Sim/src/main/antlr4\Asm8051.g4 by ANTLR 4.7

import assembler.syntaxanalyzer.Asm8051Parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link Asm8051Parser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface Asm8051Visitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#listing}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListing(Asm8051Parser.ListingContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#line}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLine(Asm8051Parser.LineContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstruction(Asm8051Parser.InstructionContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#mnemonic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMnemonic(Asm8051Parser.MnemonicContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#ajmp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAjmp(Asm8051Parser.AjmpContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#clr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClr(Asm8051Parser.ClrContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#jnz}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJnz(Asm8051Parser.JnzContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#mov}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMov(Asm8051Parser.MovContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#nop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNop(Asm8051Parser.NopContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#subb}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubb(Asm8051Parser.SubbContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#lbl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLbl(Asm8051Parser.LblContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel(Asm8051Parser.LabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(Asm8051Parser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#comment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment(Asm8051Parser.CommentContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#direct}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirect(Asm8051Parser.DirectContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#immediate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImmediate(Asm8051Parser.ImmediateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code decimal}
	 * labeled alternative in {@link Asm8051Parser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimal(Asm8051Parser.DecimalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code hexadecimal}
	 * labeled alternative in {@link Asm8051Parser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHexadecimal(Asm8051Parser.HexadecimalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binary}
	 * labeled alternative in {@link Asm8051Parser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinary(Asm8051Parser.BinaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#indirectRegister}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndirectRegister(Asm8051Parser.IndirectRegisterContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#register}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegister(Asm8051Parser.RegisterContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#accumulator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccumulator(Asm8051Parser.AccumulatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#sfr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSfr(Asm8051Parser.SfrContext ctx);
	/**
	 * Visit a parse tree produced by {@link Asm8051Parser#bit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBit(Asm8051Parser.BitContext ctx);
}