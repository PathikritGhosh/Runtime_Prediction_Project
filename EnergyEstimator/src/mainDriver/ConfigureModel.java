package mainDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigureModel {
	
	private static String configFileInputProgram, configFileGPUHardware;
	private static Properties propertiesInputProgram, propertiesGPUHardware;
	private static InputStream inputStreamProgram, inputStreamGPU;
	
	// ms
	public static double cudaContextInitializationOverhead, kernelLaunchOverhead;
	
	// MBps
	public static long transferBandwidthPeak;
	
	// const
	public static int eqCoeff1, eqConst, transferSize;
	
	// kernel
	public static int numBlocks, numThreadsPerBlock, numThreadsLaunched;
	public static double achievedOccupancy;
	
	//CUDA occupancy calculator
//	public static int maxTh, maxW, maxBl;
//	public static double maxOcc;
	public static int finalMaxBl;
	
	public static int accessFactorBadCoalescing, numBankConflicts;
	
	// GPU constants
	public static int warpSize, numBanks;
	public static int globalMemLineSize;
	public static int sharedBytesTransferred;
	public static double globalMemBandwidth, sharedMemBandwidth;
	public static long gpuClock; // MHz
	public static int maxActiveWarpsPerSM;
	public static int numSMs, numCoresPerSM, numTotalCores,maxThreadPerSm;
	
	// configure
	public static boolean testingOn;
	public static int numLoopIterations, maxNumberOfKernels, maxRegistersPerInstruction, numIndependentInsts;
	public static double branchProbability;
	public static String mainPath, benchmarkName, outputFileName;
	public static String inputProgPath, inputProgCUDA, inputProgPTX;
	
	public static void getPropValues(String gpuHardwareFile, String inputProgramFile) {
		initialize();
		configFileGPUHardware = gpuHardwareFile;
		configFileInputProgram = inputProgramFile;
		
		try {
			propertiesInputProgram = new Properties();
			propertiesGPUHardware = new Properties();
//			inputStreamProgram = ConfigureModel.class.getClassLoader().getResourceAsStream(configFileInputProgram);
			
			inputStreamProgram = new FileInputStream(new File("/home/gargi/workspace/peInputs/applications/" + inputProgramFile));
			inputStreamGPU = new FileInputStream(new File("/home/gargi/workspace/peInputs/hardware/" + gpuHardwareFile));ConfigureModel.class.getClassLoader().getResourceAsStream(configFileGPUHardware);
			if (inputStreamProgram != null) propertiesInputProgram.load(inputStreamProgram);
			else { System.out.println("Property file " + configFileInputProgram + " not found."); System.exit(0); }
			if (inputStreamGPU != null) propertiesGPUHardware.load(inputStreamGPU);
			else { System.out.println("Property file " + configFileGPUHardware + " not found."); System.exit(0); }
		} catch (Exception e) {
			System.out.println("Loading property file exception: " + e.toString());
			System.exit(0);
		}
		
		// GPU Hardware
		
		try {
			cudaContextInitializationOverhead = Double.valueOf(propertiesGPUHardware.getProperty("cudaContextInitializationOverhead").trim());
			kernelLaunchOverhead = Double.valueOf(propertiesGPUHardware.getProperty("kernelLaunchOverhead").trim());
			maxThreadPerSm= Integer.valueOf(propertiesGPUHardware.getProperty("maxThreadPerSm").trim());
			transferBandwidthPeak = Long.valueOf(propertiesGPUHardware.getProperty("transferBandwidthPeak").trim());
			gpuClock = Long.valueOf(propertiesGPUHardware.getProperty("gpuClock").trim());
			eqCoeff1 = Integer.valueOf(propertiesGPUHardware.getProperty("eqCoeff1").trim());
			eqConst = Integer.valueOf(propertiesGPUHardware.getProperty("eqConst").trim());
			warpSize = Integer.valueOf(propertiesGPUHardware.getProperty("warpSize").trim());
			globalMemLineSize = Integer.valueOf(propertiesGPUHardware.getProperty("globalMemLineSize").trim());
			globalMemBandwidth = Double.valueOf(propertiesGPUHardware.getProperty("globalMemBandwidth").trim());
			numBanks = Integer.valueOf(propertiesGPUHardware.getProperty("numBanks").trim());
			numSMs = Integer.valueOf(propertiesGPUHardware.getProperty("numSMs").trim());
			numCoresPerSM = Integer.valueOf(propertiesGPUHardware.getProperty("numCoresPerSM").trim());
			
			numTotalCores = numSMs * numCoresPerSM;
			sharedMemBandwidth = 4.0 * numBanks * numSMs * gpuClock/1.0;
			
		} catch (Exception e) {
			System.out.println("Type conversion exception: GPU Hardware file. ");
		}
		
		// Program
		
		try {
			testingOn = Boolean.valueOf(propertiesInputProgram.getProperty("testingOn").trim());
			transferSize = Integer.valueOf(propertiesInputProgram.getProperty("transferSize").trim());
			numBlocks = Integer.valueOf(propertiesInputProgram.getProperty("numBlocks").trim());
			numThreadsPerBlock = Integer.valueOf(propertiesInputProgram.getProperty("numThreadsPerBlock").trim());
			
			numThreadsLaunched = numBlocks * numThreadsPerBlock;
			
			numLoopIterations = Integer.valueOf(propertiesInputProgram.getProperty("numLoopIterations").trim());
			branchProbability = Double.valueOf(propertiesInputProgram.getProperty("branchProbability").trim());
			maxNumberOfKernels = Integer.valueOf(propertiesInputProgram.getProperty("maxNumberOfKernels").trim());
			maxRegistersPerInstruction = Integer.valueOf(propertiesInputProgram.getProperty("maxRegistersPerInstruction").trim());
			numIndependentInsts = Integer.valueOf(propertiesInputProgram.getProperty("numIndependentInsts").trim());
			maxActiveWarpsPerSM = Integer.valueOf(propertiesInputProgram.getProperty("maxActiveWarps").trim());
			accessFactorBadCoalescing = Integer.valueOf(propertiesInputProgram.getProperty("accessFactorBadCoalescing").trim());
			numBankConflicts = Integer.valueOf(propertiesInputProgram.getProperty("numBankConflicts").trim());
			sharedBytesTransferred = Integer.valueOf(propertiesInputProgram.getProperty("sharedBytesTransferred").trim());
			
			mainPath = String.valueOf(propertiesInputProgram.getProperty("mainPath").trim());
			benchmarkName = String.valueOf(propertiesInputProgram.getProperty("benchmarkName").trim());
			outputFileName = String.valueOf(propertiesInputProgram.getProperty("outputFileName").trim());
			
			inputProgPath = mainPath + benchmarkName + "/";
			inputProgCUDA = benchmarkName + ".cu";
			inputProgPTX = benchmarkName + ".ptx";
			
//			maxTh = Integer.valueOf(properties.getProperty("maxTh").trim());
//			maxW = Integer.valueOf(properties.getProperty("maxW").trim());
//			maxBl = Integer.valueOf(properties.getProperty("maxBl").trim());
//			maxOcc = Double.valueOf(properties.getProperty("maxOcc").trim());
			finalMaxBl = Integer.valueOf(propertiesInputProgram.getProperty("finalMaxBl").trim());
			achievedOccupancy = Double.valueOf(propertiesInputProgram.getProperty("achievedOccupancy").trim());
			
		} catch (Exception e) {
			System.out.println("Type conversion exception: Input program file. ");
		}
		
		try {
			inputStreamProgram.close();
			inputStreamGPU.close();
		} catch (Exception e) {
			System.out.println("Exception closing inputStream");
		}
	}
	
	public static void initialize() {
		configFileGPUHardware = "";
		configFileInputProgram = ""; propertiesInputProgram = null; propertiesGPUHardware = null;
		inputStreamProgram = null;
		inputStreamGPU = null;
		cudaContextInitializationOverhead = 0.0;
		kernelLaunchOverhead = 0.0;
		transferBandwidthPeak = 0;
		gpuClock = 0;
		eqCoeff1 = eqConst = 0;
		testingOn = false;
		sharedBytesTransferred = 0;
		warpSize = 0; maxActiveWarpsPerSM = 0;
		achievedOccupancy = 0.0;
		numBlocks = numThreadsPerBlock = numThreadsLaunched = 0;
		globalMemLineSize = 0;
		globalMemBandwidth = 0.0; sharedMemBandwidth = 0.0;
		numBanks = 0; numIndependentInsts = 0;
		numLoopIterations = 0; maxNumberOfKernels = 0; maxRegistersPerInstruction = 0;
		branchProbability = 0.0; numSMs = 0;
		mainPath = benchmarkName = outputFileName = "";
		inputProgPath = inputProgCUDA = inputProgPTX = "";
		numCoresPerSM = numTotalCores = 0;
		accessFactorBadCoalescing = numBankConflicts = 0;
//		maxTh = maxW = maxBl = 0;
//		maxOcc = 0.0;
		finalMaxBl = 0;
	}
	
}