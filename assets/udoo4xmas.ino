



/////////// Ekironji Lib ///////////////////

const int RELAY_PINS[4]  = {22, 23, 24, 25};

const int LED_STARTING_PIN = 2;
const int LED_STRIP_PINS[][3]  = {{2, 3, 4}, {5, 6, 7}, {8, 9, 10}, {11, 12, 13}};

const int RED = 0;
const int GREEN = 1;
const int BLUE = 2;

const int FIXED_COLOR   = 0x0;
const int FADING_COLOR  = 0x1;
const int BLINK_COLOR   = 0x2;
const int RAINBOW_COLOR = 0x3;

const int OP_CODE_MASK       = 0xf0000000;
const int MAIN_OP_CODE_MASK  = 0xc0000000;
const int SUB_OP_CODE_MASK   = 0x30000000;
const int ID_CODE_MASK       = 0x0f000000;
    
const int CMD_MASK     = 0xff000000;
const int RED_MASK     = 0x00ff0000;
const int GREEN_MASK   = 0x0000ff00;
const int BLUE_MASK    = 0x000000ff;
   
const int PAYLOAD_MASK = 0x00ffffff;
   
   // offsets
const int MAIN_OP_OFFSET = 30;
const int SUB_OP_OFFSET  = 28;
const int OP_OFFSET      = 28;
const int ID_OFFSET      = 24;
const int CMD_OFFSET     = 24;
const int RED_OFFSET     = 16;
const int GREEN_OFFSET   = 8;
const int BLUE_OFFSET    = 0;
      
   // MAIN op_codes
const int REQUEST_MSG   = 0x0;
const int RELAY_MSG     = 0x1;
const int STRIP_MSG     = 0x2;
const int VIDEO_MSG     = 0x3;
   
   // SUB op_codes ///////////////////////
const int REQUEST_IP_DISCOVERY_MSG   = 0x0;
const int REQUEST_SERVICE_LIST_MSG   = 0x1;
const int REQUEST_GENERIC_MSG        = 0x2;

const int PLAY_LED_DIRECT_COLOR  = 0x0;
const int PLAY_LED_FADING_COLOR  = 0x1;
const int PLAY_LED_BLINK_COLOR   = 0x2;
const int PLAY_LED_RAINBOW_COLOR = 0x3;

const int TURN_OFF_RELAY  = 0x0;
const int TURN_ON_RELAY   = 0x1;
const int SWITCH_RELAY    = 0x2;

// Executing
boolean relayState[] = {false, false, false, false};
int ledStrips[][3]   = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
int ledStripState[]  = {FIXED_COLOR, FIXED_COLOR, FIXED_COLOR, FIXED_COLOR};

// Blinking
int MAX_BLINK_CYCLES = 15;
int blinkCycles[] = {0,0,0,0};

// Rainbow
int rainbowCycles[] = {0,0,0,0};
  int tempColor[3];



void parseMessage(int msg){
  
  int opCode    = (msg >> MAIN_OP_OFFSET) & 0x00000003;
  int subOpCode = (msg & SUB_OP_CODE_MASK)  >> SUB_OP_OFFSET;
  int idCode    = (msg & ID_CODE_MASK)  >> ID_OFFSET;
  int payload[3];
  payload[0] = (msg & RED_MASK)   >> RED_OFFSET;
  payload[1] = (msg & GREEN_MASK) >> GREEN_OFFSET;
  payload[2] = (msg & BLUE_MASK)  >> BLUE_OFFSET;


  Serial.print(msg, BIN);
  Serial.print(" op: ");
  Serial.print(opCode);
  Serial.print(" subOp: ");
  Serial.print(subOpCode);
  Serial.print(" idCode: ");
  Serial.print(idCode);
  Serial.print(" payLd: ");
  Serial.print(payload[0], BIN);
  Serial.print(" ");
  Serial.print(payload[1], BIN);
  Serial.print(" ");
  Serial.print(payload[2], BIN);
  Serial.println(" ");

  switch(opCode){
  case REQUEST_MSG:
    Serial.println("Request MSG");
    switch(subOpCode){
      case REQUEST_IP_DISCOVERY_MSG:
        break;
      case REQUEST_SERVICE_LIST_MSG:
        break;
      case REQUEST_GENERIC_MSG:
        break;
    }
    break;
    
  case STRIP_MSG:
    Serial.println("Strip MSG");
    switch(subOpCode){
      case PLAY_LED_DIRECT_COLOR:
        playLedColor(idCode, payload[0], payload[1], payload[2]);
        break;
      case PLAY_LED_FADING_COLOR:
        playFadingColor(idCode, payload[0], payload[1], payload[2]);
        break;
      case PLAY_LED_BLINK_COLOR:
        playLedBlinkColor(idCode, payload[0], payload[1], payload[2]);
        break;
      case PLAY_LED_RAINBOW_COLOR:
        playRainbowColor(idCode);
        break;
    }
    break;
    
  case RELAY_MSG:
    Serial.println("Relay MSG");
    switch(subOpCode){
      case TURN_OFF_RELAY:
        turnOffRelay(idCode);
        break;
      case TURN_ON_RELAY:
        turnOnRelay(idCode);
        break;
      case SWITCH_RELAY:
        switchRelay(idCode);
        break;
    }
    break;
    
  case VIDEO_MSG:
    Serial.println("Video MSG");
    break;
  }
  
}

void playLedColor(int strip, int r, int g, int b){  
  Serial.println("playLedColor");  
  
  ledStripState[strip] = FIXED_COLOR;
  
  ledStrips[strip][RED]   = r;
  ledStrips[strip][GREEN] = g;
  ledStrips[strip][BLUE]  = b;  
}

void playFadingColor(int strip, int r, int g, int b){
  ledStripState[strip] = FADING_COLOR;
  
  ledStrips[strip][RED]   = r;
  ledStrips[strip][GREEN] = g;
  ledStrips[strip][BLUE]  = b;  
}

void playLedBlinkColor(int strip, int r, int g, int b){  
  ledStripState[strip] = BLINK_COLOR;
  blinkCycles[strip] = 0;
  
  ledStrips[strip][RED]   = r;
  ledStrips[strip][GREEN] = g;
  ledStrips[strip][BLUE]  = b;  
}

void playRainbowColor(int strip){
  Serial.println("playRainbowColor()");  
  ledStripState[strip] = RAINBOW_COLOR;
  rainbowCycles[strip] = 0;
}


void switchRelay(int relay){
  if(relayState[relay] == true)
    relayState[relay] = false;
  else
    relayState[relay] = true;
}

void turnOnRelay(int relay){
    relayState[relay] = true;
}

void turnOffRelay(int relay){
    relayState[relay] = false;
}

// Experimental :S
int getSensorValue(){}

// Utils methods
void indexToRGB(int strip, int index){
  
  if(index < 85){
    ledStrips[strip][RED]   = map(index, 0, 84, 255, 0);
    ledStrips[strip][GREEN] = 0;
    ledStrips[strip][BLUE]  = map(index, 0, 84, 0, 255);
  }
  else if(index < 170){
    ledStrips[strip][RED]   = 0;
    ledStrips[strip][GREEN] = map(index, 85, 169, 0, 255);    
    ledStrips[strip][BLUE]  = map(index, 85, 169, 255, 0);   
  }
  else if(index <= 255){
    ledStrips[strip][RED]   = map(index, 170, 255, 0, 255);
    ledStrips[strip][GREEN] = map(index, 170, 255, 255, 0);
    ledStrips[strip][BLUE]  = 0;   
  }

}

void printLedValues(){
  for(int i=0; i<4; i++){
    Serial.print(LED_STRIP_PINS[i][RED]);    
    Serial.print("  ");
    Serial.print(LED_STRIP_PINS[i][GREEN]);    
    Serial.print("  ");
    Serial.print(LED_STRIP_PINS[i][BLUE]);    
    Serial.print(" --- ");
  }
  Serial.println("");
  
  for(int i=0; i<4; i++){
    Serial.print(ledStrips[i][RED]);    
    Serial.print("  ");
    Serial.print(ledStrips[i][GREEN]);    
    Serial.print("  ");
    Serial.print(ledStrips[i][BLUE]);    
    Serial.print(" --- ");
  }
  Serial.println(".../n");
}



// RUNTIME ////////////////

void setup() {
  Serial.begin(9600);
  for(int i=2; i<=13; i++){
    pinMode(i, OUTPUT);
  }
  
  for(int i=22; i<=25; i++){
    pinMode(i, OUTPUT);
  }
}

void loop() {
  
  if(Serial.available() == 4){
    int msg = 0;
    msg |= Serial.read();
    msg |= Serial.read() << 8;
    msg |= Serial.read() << 16;
    msg |= Serial.read() << 24;
       
    parseMessage(msg);
  }
  
  
  for(int i=0; i<4; i++){
    if(relayState[i])
      digitalWrite((int) RELAY_PINS[i] , HIGH );
    else  
      digitalWrite((int) RELAY_PINS[i] , LOW );
  }
  
  for(int i=0; i<4; i++){    
    switch(ledStripState[i]){
      case FIXED_COLOR:
        analogWrite((int) LED_STRIP_PINS[i][RED]  , 255 - ledStrips[i][RED] );
        analogWrite((int) LED_STRIP_PINS[i][GREEN], 255 - ledStrips[i][GREEN] );
        analogWrite((int) LED_STRIP_PINS[i][BLUE] , 255 - ledStrips[i][BLUE] );
        break;
        
      case FADING_COLOR:       
        analogWrite((int) LED_STRIP_PINS[i][RED]  , 255 - ledStrips[i][RED] );
        analogWrite((int) LED_STRIP_PINS[i][GREEN], 255 - ledStrips[i][GREEN] );
        analogWrite((int) LED_STRIP_PINS[i][BLUE] , 255 - ledStrips[i][BLUE] );
        break;
        
      case BLINK_COLOR:
        if(blinkCycles[i] < MAX_BLINK_CYCLES){     
          analogWrite((int) LED_STRIP_PINS[i][RED]  , 255 - ledStrips[i][RED] );
          analogWrite((int) LED_STRIP_PINS[i][GREEN], 255 - ledStrips[i][GREEN] );
          analogWrite((int) LED_STRIP_PINS[i][BLUE] , 255 - ledStrips[i][BLUE] );        
        }
        else if (blinkCycles[i] >= MAX_BLINK_CYCLES && blinkCycles[i] < (MAX_BLINK_CYCLES << 1)) {             
          analogWrite((int) LED_STRIP_PINS[i][RED]  , 255 - 0 );
          analogWrite((int) LED_STRIP_PINS[i][GREEN], 255 - 0 );
          analogWrite((int) LED_STRIP_PINS[i][BLUE] , 255 - 0 );          
        }
        else
          blinkCycles[i] = 0;
        
        blinkCycles[i]++;        
        break;
        
      case RAINBOW_COLOR:
        if(rainbowCycles[i] < 255){
          
          indexToRGB(i, rainbowCycles[i]);
          analogWrite((int) LED_STRIP_PINS[i][RED]  , 255 - ledStrips[i][RED]   );
          analogWrite((int) LED_STRIP_PINS[i][GREEN], 255 - ledStrips[i][GREEN] );
          analogWrite((int) LED_STRIP_PINS[i][BLUE] , 255 - ledStrips[i][BLUE]  ); 
  
          rainbowCycles[i]++;
        }
        else  
          rainbowCycles[i] = 0;
        break; 
    }
    
  }
 
  delay(100);
  
}





