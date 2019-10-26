package programAnalyzer.instructiontypes;

import mainDriver.ConfigureModel;
import programAnalyzer.valueconstants.EnumComputeInsts;
import programAnalyzer.valueconstants.EnumMemoryInsts;
import programAnalyzer.valueconstants.EnumMiscInsts;

public final class AllInstData {
	
	public static DataComputeInst[] computeDetails;
	public static int numComputeInstTypes;
	public static DataMemoryInst[] memoryDetails;
	public static int numMemoryInstTypes;
	public static DataMiscInst[] miscDetails;
	public static int numMiscInstTypes;
	
	public static void initializeAll() {
		initializeCompute();
		initializeMemory();
		initializeMisc();
	}
	
	public static void initializeCompute() {
		numComputeInstTypes = EnumComputeInsts.values().length;
		computeDetails = new DataComputeInst[numComputeInstTypes];
		
		for (int i = 0; i < numComputeInstTypes; i++) {
			computeDetails[i] = new DataComputeInst();
		}

		// latency, throughput, peakwarps, delay
		computeDetails[0].setData(EnumComputeInsts.fmadd, "fmadd", 18, 32, 16, 0.0); 	// 18, 32, 16
		computeDetails[1].setData(EnumComputeInsts.fadd, "fadd", 10, 32, 16, 0.0); 		// 16, 32, 16
		computeDetails[2].setData(EnumComputeInsts.madd, "madd", 18, 16, 11, 0.0); 		// 22, 16, 11
		computeDetails[3].setData(EnumComputeInsts.mad, "mad", 18, 16, 11, 0.0);	 		// 22, 16, 11
		computeDetails[4].setData(EnumComputeInsts.add, "add", 10, 32, 16, 0.0); 			//16, 32, 16
		computeDetails[5].setData(EnumComputeInsts.sub, "sub", 10, 32, 16, 0.0); 			//16, 32, 16
		computeDetails[6].setData(EnumComputeInsts.fmul, "fmul", 9, 32, 16, 0.0);		 // 16, 32, 16
		computeDetails[7].setData(EnumComputeInsts.mul, "mul", 9, 16, 16, 0.0); 		// 20, 16, 16
		computeDetails[8].setData(EnumComputeInsts.fdiv, "fdiv", 866, 0.75, 4, 0.0); 	// 711, 0.75, 4
		computeDetails[9].setData(EnumComputeInsts.div, "div", 427, 1.8, 5, 0.0);		 // 317, 1.8, 5
		computeDetails[10].setData(EnumComputeInsts.and, "and", 9, 32, 16, 0.0); 		// 16, 32, 16
		computeDetails[11].setData(EnumComputeInsts.sqrt, "sqrt", 349, 1.6, 5, 0.0); 	// 269, 1.6, 5		
		computeDetails[12].setData(EnumComputeInsts.mov, "mov", 10, 32, 16, 0.0);		 // 16, 32, 16
		computeDetails[13].setData(EnumComputeInsts.setp, "setp", 10, 32, 16, 0.0);		 // 16, 32, 16
		computeDetails[14].setData(EnumComputeInsts.fma, "fma", 18, 32, 16, 0.0); 		// 16, 32, 16
		computeDetails[15].setData(EnumComputeInsts.cvt, "cvt", 9, 32, 16, 0.0); 		// 16, 32, 16

//		computeDetails[13].setData(EnumComputeInsts.ldstparam, "param", 10, 32, 16); 	// 16, 32, 16
//		computeDetails[15].setData(EnumComputeInsts.bra, "bra", 10, 32, 16); 		// 16, 32, 16
//		computeDetails[18].setData(EnumComputeInsts.ret, "ret", 10, 32, 16); 		// 16, 32, 16
	}
	
	public static void initializeMemory() {
		numMemoryInstTypes = EnumMemoryInsts.values().length;
		memoryDetails = new DataMemoryInst[numMemoryInstTypes];
		
		for (int i = 0; i < numMemoryInstTypes; i++) {
			memoryDetails[i] = new DataMemoryInst();
		}
		int totalThreads;
		double latency;
		totalThreads=ConfigureModel.numBlocks*ConfigureModel.numThreadsPerBlock;
		if(totalThreads<4096)
			latency=0.02828*totalThreads+172.9;
		else if(totalThreads<24576)
			 latency= 0.004780*totalThreads+251.7;
		else if((totalThreads>24576)&&(totalThreads<991232))
			latency=0.0001679*totalThreads+307.8;
		else if((totalThreads>991232)&&(totalThreads<2203648))
			latency=-0.00002529*totalThreads+501.8;
		else
			 latency=-0.00005988*totalThreads+574.1;
		System.out.println("Latency:"+latency);	
		//latency=0.0000706*totalThreads+269.1;
		
		//latency=300;
		
		
		// latency, peakwarps, delay
		memoryDetails[0].setData(EnumMemoryInsts.GlobalLoad, "ld.global", latency, 8, 0.0); // 305, 8
		memoryDetails[1].setData(EnumMemoryInsts.GlobalStore, "st.global",latency, 8, 0.0); // 305, 8
		memoryDetails[2].setData(EnumMemoryInsts.SharedLoad, "ld.shared", 36, 2, 0.0); // 20, 2
		memoryDetails[3].setData(EnumMemoryInsts.SharedStore, "st.global", 36, 8, 0.0); // 36, 8
		memoryDetails[4].setData(EnumMemoryInsts.ParamLoad, "ld.param", latency, 8, 0.0);
		memoryDetails[5].setData(EnumMemoryInsts.ParamStore, "st.param", latency, 8, 0.0); //481
	}
	
	public static void initializeMisc() {
		numMiscInstTypes = EnumMiscInsts.values().length;
		miscDetails = new DataMiscInst[numMiscInstTypes];
		
		for (int i = 0; i < numMiscInstTypes; i++) {
			miscDetails[i] = new DataMiscInst();
		}
		
		// latency, throughput, peakwarps, delay
		miscDetails[0].setData(EnumMiscInsts.Sync, "sync", 10, 0, 0, 0.0);
		miscDetails[1].setData(EnumMiscInsts.Label, "label", 0, 0, 0, 0.0);
		miscDetails[2].setData(EnumMiscInsts.Branch, "branch", 10, 0, 0, 0.0);
		miscDetails[3].setData(EnumMiscInsts.Return, "return", 10, 0, 0, 0.0);
	}
	
	private AllInstData() {
		throw new RuntimeException("Do not instantiate this class: " + getClass());
	}
	
}