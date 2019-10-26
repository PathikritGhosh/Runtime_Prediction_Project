package programAnalyzer.valueconstants;

public enum EnumAllInsts {
	KernelStart, KernelEnd, Label,
	Computation, MemAccess, Sync, Branch, Return,
	Comment, Directive, Empty, Unknown
}