package mainDriver;

import powerModeller.PowerPredictor;
import programAnalyzer.InputProg;
import programAnalyzer.instructiontypes.AllInstData;
import programAnalyzer.singlekernel.SingleKernel;
import executionTimeEstimator.ComputeExecTime;
import executionTimeEstimator.ComputeExecTimeModified;
import executionTimeEstimator.ComputeOverhead;

public class MainRunner {
	
	private static ComputeOverhead computeOverhead;
	@SuppressWarnings("unused")
	private static ComputeExecTimeModified computeExecModified;
	private static ComputeExecTime computeExecTime;
	private static PowerPredictor powerPredict;
	private static InputProg inputProg;
	private static String configGPUHardware = "gpuhardware/configGPUHardware.properties";
	private static String configInputProgram =
			"kmeans" + "/config" +
			"1"+ "" + ".properties";
	
	public static void initializeAll() {
		ConfigureModel.getPropValues(configGPUHardware, configInputProgram);
		AllInstData.initializeAll();
		computeOverhead = new ComputeOverhead();
		computeExecModified = new ComputeExecTimeModified();
		computeExecTime = new ComputeExecTime();
		powerPredict = new PowerPredictor();
		
		inputProg = new InputProg();
	}
		
	public static void main(String[] args) {
		int numArgs = args.length;
		for (int i = 0; i < numArgs; i++) {
			System.out.println("Arg " + i + ": " + args[i]);
		}
		initializeAll();
		runModel();
	}
	
	public static void runModel() {
		inputProg.loadComputePrint();
		
		computeOverhead.calculate(ConfigureModel.transferSize);
		
		SingleKernel curKernel = null;
		for (int i = 0; i < inputProg.getFoundNumberOfKernels(); i++) {
			curKernel = inputProg.getKernelWithNumber(i);
			computeExecTime.calculate(curKernel);
			System.out.println("Kernel " + curKernel.getKernelNumber() + "\t\tName: " + curKernel.getKernelName()); 
			System.out.println("Exec time:\t" + computeExecTime.getExecTimeTotal() + " ms");
			System.out.println("Power:\t\t" + powerPredict.getCalculatedPower(ConfigureModel.achievedOccupancy) + " W");
			System.out.println("Energy:\t\t" + computeExecTime.getExecTimeTotal() * powerPredict.getCalculatedPower(ConfigureModel.achievedOccupancy) + " mJ\n");
		}	
	}
		
	/*
	 * Any TLP related errors = check ComputeExecTime and calculateParallelism() in BasicBlock
	 */
}