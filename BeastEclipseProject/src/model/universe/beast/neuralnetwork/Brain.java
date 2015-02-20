package model.universe.beast.neuralnetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import utils.MyRandom;
import math.Angle;
import model.universe.Universe;
import model.universe.beast.Beast;
import model.universe.beast.Need;
import model.universe.beast.neuralnetwork.action.Actuator;
import model.universe.beast.neuralnetwork.action.Harvester;
import model.universe.beast.neuralnetwork.action.Mover;
import model.universe.beast.neuralnetwork.action.Rotator;
import model.universe.beast.neuralnetwork.perception.NeedSensor;
import model.universe.beast.neuralnetwork.perception.ResourceSensor;
import model.universe.beast.neuralnetwork.perception.Sensor;

public class Brain {

	public final Beast beast;
	final List<Sensor> sensors = new ArrayList<>();
	final List<Neuron> neurons = new ArrayList<>();
	final List<Actuator> actuators = new ArrayList<>();
	int serial=0;
	
	
	public Brain(Beast beast){
		this.beast = beast; 
		addNeuron(new Rotator(serial++, this, Angle.toRadians(10)));
		addNeuron(new Rotator(serial++, this, Angle.toRadians(-10)));
		addNeuron(new Rotator(serial++, this, Angle.toRadians(30)));
		addNeuron(new Rotator(serial++, this, Angle.toRadians(-30)));
		addNeuron(new Mover(serial++, this, 1));
		addNeed(beast.need);
	}

	public void addNeed(Need need){
		addNeuron(new NeedSensor(serial++, this));
		addNeuron(new ResourceSensor(serial++, this, need.resource));
		addNeuron(new Harvester(serial++, this, need.resource));
	}
	
	public Brain(Brain other, Beast newBeast){
		beast = newBeast;
		HashMap<Integer, Neuron> serializedNeurons = new HashMap<>();
		for(Sensor s : other.sensors){
			Sensor copy = (Sensor) NeuronFactory.getCopy(s, this);
			serializedNeurons.put(copy.serial, copy);
			sensors.add(copy);
		}
		for(Neuron n : other.neurons)
			neurons.add(NeuronFactory.getCopy(n, this));
		for(Actuator a : other.actuators)
			actuators.add((Actuator) NeuronFactory.getCopy(a, this));
		
	}
	
	public void stimulate(){
		for(Sensor s : sensors)
			s.stimulate();
		List<Neuron> all = new ArrayList<>();
		all.addAll(sensors);
		all.addAll(neurons);
		all.addAll(actuators);
		for(Neuron n : all)
			n.calm();
	}
	
	public void addNeuron(Sensor s){
		sensors.add(s);
	}
	public void addNeuron(Actuator a){
		actuators.add(a);
	}
	public void addNeuron(Neuron n){
		neurons.add(n);
	}
	
	public void createRandomConnexions(){
		List<Neuron> from = new ArrayList<>();
		from.addAll(sensors);
		from.addAll(neurons);
		List<Neuron> to = new ArrayList<>();
		to.addAll(neurons);
		to.addAll(actuators);
		
		for(Neuron pre : from){
			int axonCount = MyRandom.between(1, 2);
			for(int i=0; i<axonCount; i++){
				int index = MyRandom.nextInt(to.size());
				pre.launchAxonOn(to.get(index));
			}
		}
	}
	
	
	public Brain getMutation(Beast beast){
		return new Brain(this, beast);
	}
}
