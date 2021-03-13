#include "CO2Sensor.h"
#include<ArduinoJson.h>
#include<SoftwareSerial.h>
String apiKey = "5M890B6KFEZ9H86I";
SoftwareSerial nodemcu(5,6);
#define buzzer 2
float  duration;
float   distance;
const int trigPin = 11; // Trigger Pin of Ultrasonic Sensor
const int echoPin = 12; // Echo Pin of Ultrasonic Sensor
int level_filled;
// MQ-7
int gas_sensor_7 = A1; //Sensor pin 
float m_7 = -0.6527; //Slope 
float b_7 = 1.30; //Y-Intercept 
float R0_7 = 2.43;//Sensor Resistance in fresh air 
// MQ-4
int gas_sensor_4 = A3; //Sensor pin 
float m_4 = -0.35; //Slope 
float b_4 = 1.86; //Y-Intercept 
float R0_4 = 3;//Sensor Resistance in fresh air
// MQ-135
int gas_sensor_135 = A2; //Sensor pin 
float m_135 = -0.33; //Slope 
float b_135 = 0.7165; //Y-Intercept 
float R0_135 = 2.12;//Sensor Resistance in fresh air
// MQ-136
int gas_sensor_136 = A0; //Sensor pin 
float m_136 = -0.23; //Slope 
float b_136 = 0.449; //Y-Intercept 
float R0_136 = 43.5;//Sensor Resistance in fresh air
// MG_811
int gas_sensor_MG811 = A4;
CO2Sensor co2Sensor(gas_sensor_MG811, 0.99, 100);
 
void setup() {
  Serial.begin(115200); //Baud rate 
 
  pinMode(gas_sensor_7, INPUT); //Set gas sensor as input 
  pinMode(gas_sensor_4, INPUT);
  pinMode(gas_sensor_135, INPUT);
  pinMode(gas_sensor_136, INPUT);
  pinMode(gas_sensor_MG811, INPUT);
  pinMode(buzzer, OUTPUT);
  pinMode(trigPin, OUTPUT); // Sets the trigPin as an Output
  pinMode(echoPin, INPUT); // Sets the echoPin as an Input
  co2Sensor.calibrate();
   nodemcu.begin(115200);
  delay(1000);
}

// buzzer alert
 void alert()
 {
  for(int i =0;i<10;i++)
  {
  tone(buzzer, 1000); // Send 1KHz sound signal...
  delay(1000);        // ...for 1 sec
  noTone(buzzer);     // Stop sound...
  delay(1000); 
  }
 }
 // ultrasonic sensor sewage_level_detection and sms alert
 void check_sewage_level()
 {

  digitalWrite(trigPin, LOW);
      delayMicroseconds(2);
      // Sets the trigPin on HIGH state for 10 micro seconds
      digitalWrite(trigPin, HIGH);
      delayMicroseconds(10);
      digitalWrite(trigPin, LOW);
      // Reads the echoPin, returns the sound wave travel time in microseconds
      duration = pulseIn(echoPin, HIGH);
      // Calculating the distance
      distance= duration*0.034/2;
      // Prints the distance on the Serial Monitor
      Serial.print("Distance: ");
      Serial.println(distance);

      if(distance<10.0)
      {
           level_filled=1;
      }
      else
      {
         level_filled=0;
      }
 }
void loop() 
{  

 // MQ_7
   float co_ppm  =  ppm(gas_sensor_7,m_7,b_7,R0_7,10.0);
   float ch4_ppm  =  ppm(gas_sensor_4,m_4,b_4,R0_4,20.0);
   float nh4_ppm  =  ppm(gas_sensor_135,m_135,b_135,R0_135,10.0);
   float  h2s_ppm  =  ppm(gas_sensor_136,m_136,b_136,R0_136,20.0);
   float co2_ppm  =  ppm_co2(gas_sensor_MG811);

    Serial.print("PPM for CO = ");
    Serial.print(co_ppm);
    Serial.println();
    Serial.print("PPM for CH4 = ");
    Serial.print(ch4_ppm);
    Serial.println();
    Serial.print("PPM for nH4 = ");
    Serial.print(nh4_ppm);
    Serial.println();
    Serial.print("PPM for H2S = ");
    Serial.print(h2s_ppm);
    Serial.println();
    Serial.print("PPM for CO2 = ");
    Serial.print(co2_ppm);
    Serial.println();
    delay(3000);

    if(co_ppm>50 || co2_ppm>2000  ||ch4_ppm>2500 ||nh4_ppm>1000 || h2s_ppm)
    {
      alert();
    }

    check_sewage_level();
    StaticJsonDocument<1000> root;

    root["co2"] = co2_ppm;
    root["ch4"] =ch4_ppm;
    root["ch3"] = h2s_ppm;
    root["Nh4"] = nh4_ppm;
    root["co"] = co_ppm; 
    root["level"] =level_filled;
    Serial.println("level");
    Serial.println(level_filled);

    // arduino to nodemcu
    serializeJson(root, nodemcu);
    

    
    delay(10000);

}


float ppm(int gas_sensor, float m, float b, float R0, float RL)
{
  float sensor_volt; //Define variable for sensor voltage 
  float RS_gas; //Define variable for sensor resistance  
  float ratio; //Define variable for ratio
  int sensorValue = analogRead(gas_sensor); //Read analog values of sensor  
  sensor_volt = sensorValue*(5.0/1023.0); //Convert analog values to voltage 
  RS_gas = ((5.0*RL)/sensor_volt)-RL; //Get value of RS in a gas
  ratio = RS_gas/R0;  // Get ratio RS_gas/RS_air
  double ppm_log = (log10(ratio)-b)/m; //Get ppm value in linear scale according to the the ratio value  
  double ppm = pow(10, ppm_log); //Convert ppm value to log scale 
 
  return ppm;


}
float ppm_co2(int gas_sensor)
{

    float ppm = co2Sensor.read();
    return ppm;

}
