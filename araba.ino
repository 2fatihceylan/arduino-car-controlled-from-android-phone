#include <SoftwareSerial.h>


SoftwareSerial bluetoothSerial(0,1);
String veri;

const int in1=8;
const int in2=9;
const int in3=10;
const int in4=11;

void setup() {

  Serial.begin(9600);
  Serial.println("merhaba seri port");

  bluetoothSerial.begin(9600);
  bluetoothSerial.println("merhabaBluetooth");
  pinMode(in1,OUTPUT);
  pinMode(in2,OUTPUT);
  pinMode(in3,OUTPUT);
  pinMode(in4,OUTPUT);
  

}

void ileri(){
  digitalWrite(in1,HIGH);
  digitalWrite(in2,LOW);
}
void geri(){
  digitalWrite(in1,LOW);
  digitalWrite(in2,HIGH);
}
void fren(){
  digitalWrite(in1,LOW);
  digitalWrite(in2,LOW);
  digitalWrite(in3,LOW);
  digitalWrite(in4,LOW);
}

void sag(){
  digitalWrite(in3,HIGH);
  digitalWrite(in4,LOW);
}
void sol(){
  digitalWrite(in3,LOW);
  digitalWrite(in4,HIGH);
}
void orta(){
  digitalWrite(in3,LOW);
  digitalWrite(in4,LOW);
}
void dur(){
  digitalWrite(in1,LOW);
  digitalWrite(in2,LOW);
}

void loop() {
  while(Serial.available()){
    delay(40);
    char c=Serial.read();
    veri+=c;
    //Serial.print(veri);
  }
  Serial.print(veri);
  if(veri=="ileri"){
    ileri();
  }
  else if(veri=="fren"){
    fren();
  }
  else if(veri=="geri"){
    geri();
  }
  else if(veri=="sag"){
    sag();
  }
  else if(veri=="dur"){
    dur();
  }
  if(veri=="sol"){
    sol();
  }
  if(veri=="orta"){
    orta();
  }
  veri="";
  
}
