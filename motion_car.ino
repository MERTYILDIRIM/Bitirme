#include <TinyGPS.h>

#define echoPin 7
#define trigPin 8 
#define rolePin 6
long duration, distance;
String inputString = "";         // a string to hold incoming data
boolean stringComplete = false;
boolean buzzerEnabled=true;
int gpsCounter = 0;
static const uint32_t GPSBaud = 115200;//9600;
TinyGPSPlus gps;

void setup() {
  Serial.begin (9600);
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(rolePin, OUTPUT); 
  pinMode(13, OUTPUT);
  pinMode(9, OUTPUT); 
  digitalWrite(13, LOW);
}
void loop() {
  //while(true)
  //{
  //try{
  digitalWrite(13, LOW);
  if(stringComplete){
    //Serial.println(inputString.length());
    if(inputString == "b0")
    {
      //Serial.println("B0");
      buzzerEnabled = false;
      //Serial.println(buzzerEnabled);
    }
    else if(inputString == "b1")
    {
      //Serial.println("B1");
      buzzerEnabled = true;
      //Serial.println(buzzerEnabled);
    }
    if(inputString[0] == '0'){
      digitalWrite(rolePin, LOW);
      digitalWrite(13, LOW);
      analogWrite(9, 0);
    }
    else if(inputString[0] == '1'){
      while(true){
        digitalWrite(trigPin, LOW); 
        delayMicroseconds(2);     
        digitalWrite(trigPin, HIGH);
        delayMicroseconds(10);       
        digitalWrite(trigPin, LOW);
        digitalWrite(13, HIGH);
        duration = pulseIn(echoPin, HIGH);
        distance = duration/58.2;
        Serial.print("p");
        Serial.println(distance);

        if(distance < 30){
          digitalWrite(rolePin, HIGH);
          analogWrite(9, 0);
          //Serial.println("0");
        }
        else{
          digitalWrite(rolePin, LOW);
          if(distance < 50) {
            //    Serial.println(buzzerEnabled);
            if(buzzerEnabled)analogWrite(9, 200);
            //Serial.println("1");
          }
          else if(distance < 75) {
            //  Serial.println(buzzerEnabled);
            if(buzzerEnabled)analogWrite(9, 100);
            //Serial.println("1");
          }
          else if(distance < 100) {
            //Serial.println(buzzerEnabled);
            if(buzzerEnabled)analogWrite(9, 50);
            //Serial.println("1");
          }
          else {
            //Serial.println("0");
            analogWrite(9, 0);
            break;
          }
        }
        delay(500);
      }
    }
    stringComplete=false;
    inputString = "";
  }
  delay(1000);
  gpsCounter++;
          Serial.println(gpsCounter);
  if(gpsCounter == 30)
  {
    gpsCounter = 0;
    
        if (gps.location.isValid())
        {
          Serial.print("Enlem: ");
          Serial.print(gps.location.lat(), 6);
          Serial.print(" ");
          Serial.print("Boylam: ");
          Serial.println(gps.location.lng(), 6);
        }
  }
}
void serialEvent() {
  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read();
    if (inChar == '\n') {
      stringComplete = true;
      break;
    }
    // add it to the inputString:
    inputString += inChar;
    // if the incoming character is a newline, set a flag
    // so the main loop can do something about it:
  }
}


