import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import controller.Controller;
import model.DisplayStats;
import model.Slot;
import network.MulticastConnection;
import network.Network;
import network.TCPConnection;
import network.UDPConnection;

public class ParkingCashSimulator
{
    private DisplayStats displayStats;
    
    private Network network;
    private ArrayList<Sensor> sensors;
    private Controller controller;

    public ParkingCashSimulator(ArrayList<Slot> slots, Controller controller)
    {
        /**
         * Setting the packing car simulator
         *
         * The number of threads is based on the number of cores present.
         *
         * For example, a quad core processor will produce 8 threads
         */
        ParkingCash parkingCash = new ParkingCash();
        ParkingStats parkingStats = new ParkingStats(parkingCash);
        sensors = new ArrayList<>();
        this.controller = controller;
        initNetwork();
        
        displayStats = new DisplayStats();

        System.out.println("Parking Simulator\n");

        int numberOfSensors = 10;
        Thread [] threads = new Thread[numberOfSensors];
                
        //new a thread to listen server
        new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {
					String message = network.receiveMessage();
					if(message == null ) break;
					processMessage(message);
				}
			}
        	
        }).start();
        for(int i = 0; i < numberOfSensors; i++)
        {
            Sensor sensor = new Sensor(parkingStats, slots.get(i), i);
            Thread thread = new Thread(sensor);
            thread.start();
            threads[i] = thread;
            sensors.add(sensor);
        }

        /**
         * Waits for all the sensors to end
         */
        for(int i=0; i < numberOfSensors; i++)
        {
            try {
                threads[i].join();
            }
            catch (InterruptedException e)
            { }
        }

        /**
        * The results should show the number of cars to be zero and the
        * amount collected should be 2020
        *
        * If the number of cars is not zero and the amount collected is
        * not 2020 then data inconsistency is present
        */
        displayStats.setCars(parkingStats.numberOfCars);
        displayStats.setCash(parkingCash.cash);
    }

    
    
	private void processMessage(String message) {
    	/*
    	 * structure of message:
    	 * @command:@index:@clientID
    	 */
		String[] info = message.split(":");
		String command = info[0];
		int index = Integer.parseInt(info[1]);
		switch(command) {
		case "ComeIn":
			this.sensors.get(index).carComeIn();
			break;
		case "ComeOut":
			this.sensors.get(index).carComeOut();
			break;
			default:
				System.out.println("Unknown Command --> " + message);
				break;
		}
		
	}


    

	private void initNetwork() {
    	//get network type from console
    	Scanner sc = new Scanner(System.in);
    	System.out.println("Please Enter Connection Type:");
    	System.out.println("1. TCP");
    	System.out.println("2. UDP");
    	System.out.println("3. Multicast");
    	int type = sc.nextInt();
    	switch(type)
    	{
    	case 1:
    		network = new TCPConnection();
    		break;
    	case 2:
    		network = new UDPConnection();
    		break;
    	case 3:
    		network = new MulticastConnection();
    		break;
    		default:
    			System.out.println("Invalid Input");
    			initNetwork();
    			break;
    	}
    	network.initialize(controller);
    	sc.close();
	}

	public DisplayStats getDisplay()
    {
        return displayStats;
    }

    private class ParkingCash
    {
        /**
         * ParkingCash is responsible for initialising the cost and cash as well as collecting
         * the cash when the car pays. Finally the print method show how much money was
         * made.
         */
        private static final int COST = 2;
        private long cash;

        public ParkingCash()
        {
            cash = 0;
        }

        public void vehiclePay()
        {
            cash = cash + COST;
        }

        public void print()
        {
            System.out.println("Closing Account");
            long totalAmount;
            totalAmount = cash;
            cash = 0;
            System.out.println("The total amount is " + totalAmount);
        }
    }

    private class ParkingStats
    {
        /**
         * ParkingStats is responsible for recording a car coming into the packing
         * lot and then when a car leaves, the car pays. The ParkingCash object pays
         * for the car.
         */
        private long numberOfCars;

        private ParkingCash parkingCash;

        public ParkingStats(ParkingCash parkingCash)
        {
            numberOfCars = 0;
            this.parkingCash = parkingCash;
        }

        public void carComeIn()
        {
            numberOfCars++;
        }

        public void carComeOut()
        {
            numberOfCars--;
            parkingCash.vehiclePay();
        }
    }

    public class Sensor implements Runnable
    {
        /**
         * Each sensor will simulate three coming in and three cars coming
         *
         * Each sensor will sleep for a period of time, where another thread
         * might have the opportunity to modify another thread data.
         *
         * If this happens then data inconsistency will occur.
         */
        private ParkingStats parkingStats;
        private Slot slot;
        private int index;
        private String sensorId;

        public Sensor(ParkingStats parkingStats, Slot slot, int index)
        {
            this.parkingStats = parkingStats;
            this.slot = slot;
            this.index = index;
            initSensorId();
        }


        private void initSensorId() {
        	Random r = new Random();
        	String id = r.nextLong()+""+System.currentTimeMillis();
        	sensorId = id;			
		}


		public void run()
        {
        	
        	/*
        	 * As in the original version, the simulator do all performances by itself
        	 * In C/S model, the client needs to send a request to server, and waiting for 
        	 * response from server. then the client acts according to response
        	 */
            int numberOfCycles = 10;

            while(numberOfCycles > 0) {
            	requestComeIn();

                sleep((long) (Math.random() * 250));
                
                requestComeOut();

                sleep((long) (Math.random() * 250));

                numberOfCycles--;
            }
        }
        
        public void carComeOut() {
          parkingStats.carComeOut();
          slot.carComeOut();
        }
        
        public void carComeIn() {
          parkingStats.carComeIn();
          slot.carComeIn();
        }
        
        private void requestComeOut() {
	
        	StringBuffer message = new StringBuffer();
        	message.append("ComeOut:");
        	message.append(index+":");
        	message.append(sensorId);
        	network.sendMessage(message.toString());
		}

		private void requestComeIn() {

        	/*
        	 * structure of message:
        	 * @command:@index:@clientID
        	 */
        	StringBuffer message = new StringBuffer();
        	message.append("ComeIn:");
        	message.append(index+":");
        	message.append(sensorId);
        	network.sendMessage(message.toString());
		}
		
        	
    }

    private void sleep(long ms)
    {
        try
        {
            Thread.sleep(ms); // Sleeps are in milliseconds
        }
        catch (InterruptedException e)
        {

        }
    }
}
