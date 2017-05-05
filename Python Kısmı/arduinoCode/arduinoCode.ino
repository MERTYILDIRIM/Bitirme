String inputString = "";         // a string to hold incoming data
boolean stringComplete = false;  // whether the string is complete

void setup()
{
  // start serial port at 9600 bps:
  Serial.begin(9600);
  inputString.reserve(200);

  pinMode(13, OUTPUT);   // digital sensor is on digital pin 2
  digitalWrite(13, HIGH);
}

void loop()
{
  if (stringComplete) {
    if(inputString=="1")
      digitalWrite(13, HIGH);
    else if(inputString=="0")
      digitalWrite(13, LOW);
    
    inputString = "";
    stringComplete = false;
    delay(5000);
  }
}
void serialEvent() {
  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read(); 
    // add it to the inputString:
    inputString += inChar;
    // if the incoming character is a newline, set a flag
    // so the main loop can do something about it:
    if (inChar == '\n') {
      stringComplete = true;
    } 
  }
}
