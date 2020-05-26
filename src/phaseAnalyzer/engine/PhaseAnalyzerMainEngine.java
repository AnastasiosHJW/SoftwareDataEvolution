package phaseAnalyzer.engine;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import phaseAnalyzer.analysis.IPhaseExtractor;
import phaseAnalyzer.analysis.PhaseExtractorFactory;
import phaseAnalyzer.commons.PhaseCollector;
import phaseAnalyzer.commons.TransitionHistory;
import phaseAnalyzer.parser.IParser;
import phaseAnalyzer.parser.ParserFactory;
import data.dataKeeper.ClusterManager;
import data.dataPPL.pplTransition.PPLTransition;

public class PhaseAnalyzerMainEngine {

	public PhaseAnalyzerMainEngine(String inputCsv,String outputAssessment1,String outputAssessment2,ClusterManager clusterManager){
		
		timeWeight=clusterManager.getTimeWeight();
		changeWeight=clusterManager.getChangeWeight();
		//this.changeWeight = changeWeight;
		preProcessingTime=clusterManager.getPreProcessingTime();
		preProcessingChange=clusterManager.getPreProcessingChange();
		
		this.inputCsv=inputCsv;
		
		parserFactory = new ParserFactory();
		parser = parserFactory.createParser("SimpleTextParser");

		phaseExtractorFactory = new PhaseExtractorFactory();
		phaseExtractor = phaseExtractorFactory.createPhaseExtractor("BottomUpPhaseExtractor");
		
		transitionHistory = new TransitionHistory();
		
		allPhaseCollectors = new HashMap<String, ArrayList<PhaseCollector>>();
		
	}

	public ArrayList<PhaseCollector> getPhaseCollectors(){
		return phaseCollectors;
	}
	
	public void extractPhases(int numPhases){
		phaseCollectors = new ArrayList<PhaseCollector>();
		
		PhaseCollector phaseCollector = new PhaseCollector();
		phaseCollector = phaseExtractor.extractAtMostKPhases(transitionHistory, numPhases,timeWeight,changeWeight,preProcessingTime,preProcessingChange);
		phaseCollectors.add(phaseCollector);
		
		allPhaseCollectors.put(inputCsv, phaseCollectors);
	}
	
	public void connectTransitionsWithPhases(TreeMap<Integer,PPLTransition> PPLTransitions){
		phaseCollectors.get(0).connectPhasesWithTransitions(PPLTransitions);
	}
	
	public void parseInput(){

		this.transitionHistory = parser.parse(inputCsv, ";"); 
		this.transitionHistory.consoleVerticalReport();
	}
	
	private ParserFactory parserFactory;
	private IParser parser;
	private PhaseExtractorFactory phaseExtractorFactory;
	private IPhaseExtractor phaseExtractor;
	private TransitionHistory transitionHistory;
	
	private ArrayList<PhaseCollector> phaseCollectors;
	private HashMap<String,ArrayList<PhaseCollector>> allPhaseCollectors;
	private String inputCsv;
	//private String outputAssessment1;
	//private String outputAssessment2;
	private float timeWeight;
	private float changeWeight;
	private boolean preProcessingTime;
	private boolean preProcessingChange;
	
	
}
