#define echoPin 7
#define trigPin 8 
#define rolePin 6
long duration, distance;
String inputString = "";         // a string to hold incoming data
boolean stringComplete = false;
boolean buzzerEnabled=true;
boolean relayEnabled=true;
int warningDistance = 100;
int referansDegeri = 800;
int buzzerLevel;
String sol, sag;
int solDeger, sagDeger, sayac = 0;
void setup() {
  Serial.begin (9600);
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(rolePin, OUTPUT); 
  pinMode(13, OUTPUT);
  pinMode(9, OUTPUT); 
  digitalWrite(13, LOW);
  Serial.println("s0");
}
void loop() {
  lineCheck();
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
    else if(inputString == "r0")
    {
      //Serial.println("B0");
      relayEnabled = false;
      //Serial.println(buzzerEnabled);
    }
    else if(inputString == "r1")
    {
      //Serial.println("B1");
      relayEnabled = true;
      //Serial.println(buzzerEnabled);
    }
    else if(inputString.toInt() >= 100)
    {
      //Serial.println("B1");
      warningDistance = inputString.toInt();
      //Serial.println(buzzerEnabled);
    }
    else if(inputString[0] == '0'){
      digitalWrite(rolePin, LOW);
      digitalWrite(13, LOW);
      analogWrite(9, 0);
      buzzerLevel = 0;
    }
    else if(inputString[0] == '2'){
      stringComplete=false;
      inputString = "";
      int preDistance = 0;
      while(true){
        lineCheck();
        settingsCheck();
        //digitalWrite(trigPin, LOW); 
        //delayMicroseconds(2);     
        digitalWrite(trigPin, HIGH);
        delayMicroseconds(20);       
        digitalWrite(trigPin, LOW);
        digitalWrite(13, HIGH);
        duration = pulseIn(echoPin, HIGH);
        distance = duration/58.2;
        Serial.print("h");
        Serial.println(distance);
        if(!(distance < preDistance + 1 && distance > preDistance - 1))
          if(distance < warningDistance/10*3 && distance < preDistance){
            if(relayEnabled)digitalWrite(rolePin, HIGH);
            analogWrite(9, 0);
            buzzerLevel = 0;
            //Serial.println("0");
          }
        else{
          relayEnabled = true;
          digitalWrite(rolePin, LOW);
          if(distance < warningDistance/2) {
            //    Serial.println(buzzerEnabled);
            if(buzzerEnabled)analogWrite(9, 200);
            buzzerLevel = 200;
            //Serial.println("1");
          }
          else if(distance < warningDistance*3/4) {
            //  Serial.println(buzzerEnabled);
            if(buzzerEnabled)analogWrite(9, 100);
            buzzerLevel = 100;
            //Serial.println("1");
          }
          else if(distance < warningDistance) {
            //Serial.println(buzzerEnabled);
            if(buzzerEnabled)analogWrite(9, 50);
            buzzerLevel = 50;
            //Serial.println("1");
          }
          else {
            //Serial.println("0");
            analogWrite(9, 0);
            buzzerLevel = 0;
            break;
          }
        }
        preDistance = distance;
        delay(500);
      }
    }
    else if(inputString[0] == '1'){
      stringComplete=false;
      inputString = "";
      int preDistance = 0;
      while(true){
        lineCheck();
        settingsCheck();
        //digitalWrite(trigPin, LOW); 
        //delayMicroseconds(2);     
        digitalWrite(trigPin, HIGH);
        delayMicroseconds(20);       
        digitalWrite(trigPin, LOW);
        digitalWrite(13, HIGH);
        duration = pulseIn(echoPin, HIGH);
        distance = duration/58.2;
        Serial.print("p");
        Serial.println(distance);
        if(!(distance < preDistance + 1 && distance > preDistance - 1))
          if(distance < warningDistance/10*3 && distance < preDistance){
            if(relayEnabled)digitalWrite(rolePin, HIGH);
            analogWrite(9, 0);
            buzzerLevel = 0;
            //Serial.println("0");
          }
        else{
          relayEnabled = true;
          digitalWrite(rolePin, LOW);
          if(distance < warningDistance/2) {
            //    Serial.println(buzzerEnabled);
            if(buzzerEnabled)analogWrite(9, 200);
            buzzerLevel = 200;
            //Serial.println("1");
          }
          else if(distance < warningDistance*3/4) {
            //  Serial.println(buzzerEnabled);
            if(buzzerEnabled)analogWrite(9, 100);
            buzzerLevel = 100;
            //Serial.println("1");
          }
          else if(distance < warningDistance) {
            //Serial.println(buzzerEnabled);
            if(buzzerEnabled)analogWrite(9, 50);
            buzzerLevel = 50;
            //Serial.println("1");
          }
          else {
            //Serial.println("0");
            analogWrite(9, 0);
            buzzerLevel = 0;
            break;
          }
        }
        preDistance = distance;
        delay(500);
      }
    }
    stringComplete=false;
    inputString = "";
  }
  Serial.println('c');
  delay(1000);
}
void settingsCheck()
{
  serialEvent();
        if(stringComplete)
        {
          if(inputString == "b0")
          {
            //Serial.println("B0");
            buzzerEnabled = false;
            analogWrite(9, 0);
            //Serial.println(buzzerEnabled);
          }
          else if(inputString == "b1")
          {
            //Serial.println("B1");
            buzzerEnabled = true;
            analogWrite(9, buzzerLevel);
            //Serial.println(buzzerEnabled);
          }
          else if(inputString == "r0")
          {
            //Serial.println("B0");
            relayEnabled = false;
            digitalWrite(rolePin, LOW);
            //Serial.println(buzzerEnabled);
          }
          else if(inputString == "r1")
          {
            //Serial.println("B1");
            relayEnabled = true;
            //Serial.println(buzzerEnabled);
          }
          else if(inputString.toInt() >= 100)
          {
            //Serial.println("B1");
            warningDistance = inputString.toInt();
            //Serial.println(buzzerEnabled);
          }
          else
          {
            return;
          }
          stringComplete=false;
          inputString = "";
        }
}
void lineCheck()
{
  solDeger = analogRead(A3);
  sagDeger = analogRead(A0);
  if(solDeger < referansDegeri){
    //Serial.println("siyah");
    sol = "siyah";
  }else{
    //Serial.println("beyaz");
    sol = "beyaz";
  }
  if(sagDeger < referansDegeri){
    //Serial.println("siyah");
    sag = "siyah";
  }else{
    //Serial.println("beyaz");
    sag = "beyaz";
  }
  if(sol == "beyaz" && sag == "beyaz")
  {
    sayac++;
    if(sayac==10)
    {
      //Serial.println("SERIT IHLALI");
      Serial.println("s1");
    }
    /*else
    {
      Serial.print("SERIT IHLALI ICIN ");
      Serial.println(10-sayac);
    }*/
  }
  else
  {
    if(sayac!=0)
    {
      sayac=0;
      Serial.println("s0");
    }
    //Serial.println("Sag: "+sag+" Sol: "+sol);
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





