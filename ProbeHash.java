import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.awt.geom.Rectangle2D;

//hash table using Linear probing
//Written by Dan Reed 11/23/11

public class ProbeHash {
	private class Entry
	{
		private String key;
		private String value;
		public int generation;
		public int collide;
		
		public Entry(String key, String value, int generation, int collision)
		{
			this.key = key;
			this.value = value;
			this.generation  = generation;
			this.collide = collision;
		}
	}
	
	private Entry[] table;
	private int size;
	private int count;
	private final int INITIAL_CAPACITY = 1000000;
	private final int AMOUNT_OF_ENTRIES =850000;
	private final double LOAD_THRESHOLD = 1.01;
	private final Entry DELETED = new Entry(null, null, 0, 0);
	public int probes=0;
	
	//picture variables
	int MARK_SIZE = 1;
    int WINDOW_WIDTH = 1050;
    int WINDOW_HEIGHT = 1050;
    int BORDER_SIZE = 25;
    int ROW_LENGTH = 1000;
    int GIF_FRAME_FREQUENCY = 25000; //use ~1k for final values
	
	public ProbeHash()
	{
		this.size = INITIAL_CAPACITY;
		this.count = 0;
		this.table = new Entry[size];
	}
	
	//searches for value with given key in the hash table
	//returns value if found, returns null if not found
	public String find(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException
	{
		int i, initial, current;
		String answer = null;
		initial = Math.abs(hash(key)) % size;
		//had to add this to offset my crappy hash function
		if(initial<0)
			initial = Math.abs(initial);
		
		for(i=0;i<size;i++)
		{
			current = (initial + i) % size;
			probes++;
			if(table[current] == null)
				break;
			else if(key.equals(table[current].key))
			{
				answer = table[current].value;
				break;
			}
			else;
		}
		return answer;
	}
	
	//inserts a key-value pair into hash table, dupes not allowed
	//if key already exists then repalces old with new value, returns
	//old value or null
	public String insert(String key, String value, int generation) throws UnsupportedEncodingException, NoSuchAlgorithmException
	{
		int i, initial, current;
		String oldValue = null;
		
		if((double)count/size >= LOAD_THRESHOLD)
			rehash();
		
		initial = Math.abs(hash(key)) %size;
		//had to add this to offset my crappy hash function
		if(initial<0)
			initial = Math.abs(initial);
		int probed=0;
		for(i=0;i<size;i++)
		{
			probes++;
			current = (initial+i) %size;
			if(table[current] == null)
			{
				table[current] = new Entry(key, value, generation, probed);
				count = count+1;
				break;
			}
			else if(key.equals(table[current].key))
			{
			    //System.out.println("COLLISION!");
				oldValue = table[current].value;
				table[current].value = value;

				break;
			}
			else;
			probed +=1;
		}
		return oldValue;
	}
	
	//deletes key-value pair with given key from hash table
	//returns deleted value or null
	public String delete(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException
	{
		int i, initial, current;
		String deletedValue = null;
		
		initial = Math.abs(hash(key)) % size;
		//had to add this to offset my crappy hash function
		if(initial<0)
			initial = Math.abs(initial);
		for(i=0;i<size;i++)
		{
			probes++;
			current = (initial +i) % size;
			if(table[current] == null)
				break;
			else if(key.equals(table[current].key))
			{
				deletedValue = table[current].value;
				table[current] = DELETED;
				break;
			}
			else;
		}
		return deletedValue;
	}
	//rehashes the table
	private void rehash() throws UnsupportedEncodingException, NoSuchAlgorithmException
	{
		Entry[] oldTable = table;
		int oldSize = size;
		
		table = new Entry[2*oldSize];
		size = 2*oldSize;
		count =0;
		
		for(int i=0;i<oldSize;i++)
			if(oldTable[i] != null && oldTable[i].key != null)
				insert(oldTable[i].key, oldTable[i].value, oldTable[i].generation);
	}
	//prints out value of table data at n passed
	public int printData(int n)
	{
		if(table[n] != null)
		{
			System.out.print(table[n].value + " ");
			return 1;
		}
		else
			return 0;
	}
	//this actuall calls another hashing method
	public int hash(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException
	{
	    return hash1(key);
	    //return hash2(key);
	    //return hash3(key);
		//return hash4(key);
		//return hash5(key);
	}
	
	//java hashcode
	public int hash1(String key)
	{
		return key.hashCode();
	}
	
	
	//SHA-1
	public int hash2(String key)throws UnsupportedEncodingException, NoSuchAlgorithmException
	{
		int h=0;
		//here, in the getBytes, it was recommended to choose an encoding type
		//i.e. "UTF-8" or something like that
		//but anyway, take our key string and grab it's bytes
		byte[] bytesOfMessage = key.getBytes();

		MessageDigest md = MessageDigest.getInstance("SHA-1");
		
		//make a new byte array to hold the digested bytes from our key
		byte[] thedigest = md.digest(bytesOfMessage);
		//for each byte in the digested array
		for(int i=0;i<thedigest.length;i++)
		{
			//offset h with a dirty hex->int conversion
			//h = h + (16^i)*(byte's int value 0-15)
			h+= (int)(Math.pow(16, i))*(thedigest[i] & 0xFF);
		}
		//return our (hopefully unique) value
		return h;

	}
	
	//MD5
	public int hash3(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException
	{
		int h=0;
		//here, in the getBytes, it was recommended to choose an encoding type
		//i.e. "UTF-8" or something like that
		//but anyway, take our key string and grab it's bytes
		byte[] bytesOfMessage = key.getBytes();

		//this is a default Java library that gets ready for a standard encoding process
		//it also accepts a variety of encoding options, not just MD5
		MessageDigest md = MessageDigest.getInstance("MD5");
		
		//make a new byte array to hold the digested bytes from our key
		byte[] thedigest = md.digest(bytesOfMessage);
		//for each byte in the digested array
		for(int i=0;i<thedigest.length;i++)
		{
			//offset h with a dirty hex->int conversion
			//h = h + (16^i)*(byte's int value 0-15)
			h+= (int)(Math.pow(16, i))*(thedigest[i] & 0xFF);
		}
		//return our (hopefully unique) value
		return h;
	}
	//jenkins hash
	public int hash4(String key)
	{
		//start out with our h value
		int h=0;
		
		//for each character in the key
    	for(int i = 0; i < key.length(); ++i)
    	{
    		//offset the hash with the value for the character
        	h += key.charAt(i);
        	//h is now itself plus itself bitshifted.
        	h += (h << 10);
        	//now XOR it with itself shifted another way
        	h ^= (h >> 6);
    	}
    	//once out of the loop, we add it to its bitshifted self
    	h += (h << 3);
    	//and xor it again
    	h ^= (h >> 11);
    	//and add it again
    	h += (h << 15);
    	//hopefully with all that shifting and moving around,
    	//by now h is pretty good and unique
    	return h;
	}
	public int hash5(String key) {
       char ch[];
       ch = key.toCharArray();

       int sum=0;
       for (int i=0; i < key.length(); i++)
         sum += ch[i];
       return sum;
     }
	public void doSomeHashing(){
	    
	    AnimatedGifEncoder enc = new AnimatedGifEncoder();
        enc.start("animationProbeTest.gif");
        enc.setDelay(50);   // 25 frames per sec==40
        enc.setRepeat(0);
        int currentGeneration =0;
	    
	    for(int i=0;i< AMOUNT_OF_ENTRIES;i++){
	        String myString = generateRandomString();
	        try{
	            insert(myString, myString, currentGeneration);
	            //make some gif stuff
	            if(i%GIF_FRAME_FREQUENCY==0){
	                currentGeneration++;
	                System.out.println("moving on to the next generation of buckets: " + currentGeneration);
                    enc.addFrame(getFrameSnapshot());
                }
            }
            catch (Exception e){
                System.out.println("unsupported encoding exception.");
            }
	    }
	    System.out.println("last generation: " + currentGeneration);
	    System.out.println("average amount of probes: " + (double)probes/(double)AMOUNT_OF_ENTRIES);
	    enc.finish();
	    createPicture("staticProbeTest.png");
	    createPictureKey("staticProbeKey.png");
	}
	public void createPicture(String filename){
        // Create an image to save
        RenderedImage rendImage = getPicture();

        // Write generated image to a file
        try {
            // Save as PNG
            File file = new File(filename);
            ImageIO.write(rendImage, "png", file);

            // Save as JPEG
            //file = new File("newimage.jpg");
            //ImageIO.write(rendImage, "jpg", file);
        } catch (IOException e) {
        }
    }
    
    public void createPictureKey(String fileName){
        // Create an image to save
        RenderedImage rendImage = getPictureKey();

        // Write generated image to a file
        try {
            // Save as PNG
            File file = new File(fileName);
            ImageIO.write(rendImage, "png", file);

            // Save as JPEG
            //file = new File("newimage.jpg");
            //ImageIO.write(rendImage, "jpg", file);
        } catch (IOException e) {
        }
    }
    public RenderedImage getPictureKey(){
        int numGenerations = this.size/GIF_FRAME_FREQUENCY;
        int boxDimension = 12;
        
        // Create a buffered image in which to draw
        BufferedImage bufferedImage = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB);

        // Create a graphics contents on the buffered image
        Graphics g = bufferedImage.createGraphics();
        g.setColor(Color.pink);
        g.fillRect(0,0, WINDOW_WIDTH, WINDOW_HEIGHT);
        
        g.setColor(Color.black);
        g.drawString("Graph Key", WINDOW_WIDTH/2, 25);
        g.drawString("Non-Colliding buckets", (130+BORDER_SIZE), (130+BORDER_SIZE));
        g.drawString("Collision Buckets", (730+BORDER_SIZE), (130+BORDER_SIZE));
        g.drawString("By Dan Reed", 500, 800);
        
        //time lapse gradient
        for(int i=0;i<numGenerations;i++){
            g.setColor(getColor(i, 0));
            g.fillRect((150+BORDER_SIZE),(150+i*boxDimension+BORDER_SIZE),boxDimension, boxDimension);
            g.setColor(Color.black);
            g.drawString("Generation: " + i, (150+BORDER_SIZE+boxDimension), (150+i*boxDimension+BORDER_SIZE)+boxDimension);
        }
        //grayscale collision gradient
        for(int j=1;j<numGenerations;j++){
            g.setColor(getColor(j, j));
            g.fillRect((750+BORDER_SIZE),(150+j*boxDimension+BORDER_SIZE),boxDimension, boxDimension);
            g.setColor(Color.black);
            g.drawString("Probes: " + j, (750+BORDER_SIZE+boxDimension), (150+j*boxDimension+BORDER_SIZE+boxDimension)); 
        }
        return bufferedImage;
    }
    
    public RenderedImage getPicture() {
       
        // Create a buffered image in which to draw
        BufferedImage bufferedImage = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB);

        // Create a graphics contents on the buffered image
        Graphics g = bufferedImage.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0,0, WINDOW_WIDTH, WINDOW_HEIGHT);
        g.setColor(Color.red);
        // Draw graphics
        for(int i=0;i<this.size;i++){
            if(table[i]!=null){
                //if there has been a collision, set the color to collision color
                g.setColor(getColor(table[i].generation, table[i].collide));
                g.fillRect((i%ROW_LENGTH+BORDER_SIZE),(i/ROW_LENGTH+BORDER_SIZE),MARK_SIZE, MARK_SIZE);
            }
        }
        
        // Graphics context no longer needed so dispose it
        g.dispose();

        return bufferedImage;
    }
    
    public BufferedImage getFrameSnapshot() {
       
        // Create a buffered image in which to draw
        BufferedImage bufferedImage = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB);

        // Create a graphics contents on the buffered image
        Graphics g = bufferedImage.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0,0, WINDOW_WIDTH, WINDOW_HEIGHT);
        //g.setColor(Color.red);
        
        // Draw graphics
        for(int i=0;i<this.size;i++){
            if(table[i]!=null){
                g.setColor(getColor(table[i].generation, table[i].collide));
                
                g.fillRect((i%ROW_LENGTH+BORDER_SIZE),(i/ROW_LENGTH+BORDER_SIZE),MARK_SIZE, MARK_SIZE);
            }
        }
        
        // Graphics context no longer needed so dispose it
        g.dispose();

        return bufferedImage;
    }
    public Color getColor(int generation, int collide){
        if(collide>0)
            return getCollisionColor(collide);
        else    
            return getGenerationColor(generation);
    }
    public Color getCollisionColor(int numProbes){
        int numGenerations = this.size/GIF_FRAME_FREQUENCY;
        
        int redValue=0;
        int blueValue=0;
        int greenValue= 0;
        double colorOffset = ((double)255/(double)numGenerations)
                            *((double)this.size/(double)AMOUNT_OF_ENTRIES);
        redValue = 227 - (int)((colorOffset * (double)numProbes)*.85);
        greenValue = 223 - (int)((colorOffset * (double)numProbes)*.85);
        blueValue = 223 - (int)((colorOffset * (double)numProbes)*.85);
        
        if(redValue<0)
            redValue =0;
        else if(redValue>255)
            redValue = 255;
        if(greenValue<0)
            greenValue =0;
        else if(greenValue>255)
            greenValue = 255;
        if(blueValue<0)
            blueValue =0;
        else if(blueValue>255)
            blueValue = 255;
        
        Color myColor = new Color(redValue, greenValue, blueValue);

        return myColor;
    }
    public Color getGenerationColor(int generation){
        
        int numGenerations = this.size/GIF_FRAME_FREQUENCY;
        
        int redValue=0;
        int blueValue=0;
        int greenValue= 0;
        double colorOffset = ((double)255/(double)numGenerations)
                            *((double)this.size/(double)AMOUNT_OF_ENTRIES);
        redValue = 127 + (int)((colorOffset * (double)generation)/2.0);
        greenValue = 0 + (int)(colorOffset * (double)generation);
        blueValue = 255 - (int)(colorOffset * (double)generation);
        
        if(redValue<0)
            redValue =0;
        else if(redValue>255)
            redValue = 255;
        if(greenValue<0)
            greenValue =0;
        else if(greenValue>255)
            greenValue = 255;
        if(blueValue<0)
            blueValue =0;
        else if(blueValue>255)
            blueValue = 255;
        
        //System.out.println("for generation " + generation + " RGB is: " + redValue 
        //    + " "+ greenValue + " " + blueValue);
        Color myColor = new Color(redValue, greenValue, blueValue);
        return myColor;
    }
    
    //generates a random 20 letter string of alphanumeric characters 0-9,a-z, A-z
    //62^20 possible output strings = 10^35 possibilities
    public String generateRandomString(){
        java.util.Random r = new java.util.Random();
           
            char c;
            int charIntValue;
            String str="";
            for (int t = 0; t < 20; t++) {
                int tmp = r.nextInt(62);
                if(tmp<10){
                    charIntValue = tmp+48;
                }
                else if(tmp<36){
                    charIntValue = tmp+55;
                }
                else{
                    charIntValue = tmp+61;
                }
                c=(char)charIntValue;
                
                str= String.valueOf(c)+str;
            }
        //System.out.println(str);
        return str;
    }
}