#include<ArduinoJson.h>
#include<SoftwareSerial.h>
#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>

// Set these to run example.  
#define FIREBASE_HOST "iot-manhole-entry-gas-detectio.firebaseio.com"  
#define FIREBASE_AUTH "CLnIQSDwjmkmYTNOR83RkLssjagPjusz9xJjIVmV"  
#define WIFI_SSID "sanj30ay"  
#define WIFI_PASSWORD "12345678"  

FirebaseData firebaseData;
#include <ThingSpeak.h>;
SoftwareSerial nodemcu(D6,D5);
const char* APIKey = "5UJMNR7E70RZ7MT2";    //  Enter your Write API key from ThingSpeak
#define HOSTIFTTT "maker.ifttt.com"
const char *ssid =  "sanj30ay";     // replace with your wifi ssid and wpa2 key
const char *pass =  "12345678";
const char* host = "api.thingspeak.com";

unsigned long myChannelNumber = 1102451;

unsigned long previousMillis = 0; 
const long interval = 1000;

String MakerIFTTT_Key ;
String MakerIFTTT_Event;
char *append_str(char *here, String s) {  int i=0; while (*here++ = s[i]){i++;};return here-1;}
char *append_ul(char *here, unsigned long u) { char buf[20]; return append_str(here, ultoa(u, buf, 10));}
char post_rqst[256];char *p;char *content_length_here;char *json_start;int compi;
WiFiClient client;
WiFiClientSecure client1;
void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
   delay(10);
 
       Serial.println("Connecting to ");
       Serial.println(ssid);
 
 
       WiFi.begin(ssid, pass);
 
      while (WiFi.status() != WL_CONNECTED) 
     {
            delay(500);
            Serial.print(".");
     }
      Serial.println("");
      Serial.println("WiFi connected");
       Serial.println();  
  Serial.print("connected: ");  
  Serial.println(WiFi.localIP());  
    
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);  
  Firebase.reconnectWiFi(true);
  delay(1000);
  nodemcu.begin(115200);
  int i;
  while(!Serial) continue;

}
 int i=484;
 int t;
 int h;
 String gas;

void ses_sms()
{
      
  if (client.connect("maker.ifttt.com",80)) {
    MakerIFTTT_Key ="lB7Jl7ycY3rIwA_S9tTRxDAjnXqr6F7nmUuaDUSWmT";
    MakerIFTTT_Event ="filled";
    p = post_rqst;
    p = append_str(p, "POST /trigger/");
    p = append_str(p, MakerIFTTT_Event);
    p = append_str(p, "/with/key/");
    p = append_str(p, MakerIFTTT_Key);
    p = append_str(p, " HTTP/1.1\r\n");
    p = append_str(p, "Host: maker.ifttt.com\r\n");
    p = append_str(p, "Content-Type: application/json\r\n");
    p = append_str(p, "Content-Length: ");
    content_length_here = p;
    p = append_str(p, "NN\r\n");
    p = append_str(p, "\r\n");
    json_start = p;
    compi= strlen(json_start);
    content_length_here[0] = '0' + (compi/10);
    content_length_here[1] = '0' + (compi%10);
    client.print(post_rqst); 
    delay(10000); 
}
}
void loop() {
    
      StaticJsonDocument<1000> root;
      DeserializationError error = deserializeJson(root,nodemcu);

      while(error)
      {
        error = deserializeJson(root,nodemcu);
      }
       int a =(int)(root["co"]);//c0
       int c =(int)(root["co2"]);//c02
       int d =(int)(root["ch4"]);// ch4
       int e =(int)(root["ch3"]); //h2s
       int f =(int)(root["Nh4"]);//ch3
       int v = (int)(root["level"]);
       if(v==1)
       {
          ses_sms();
       }
       Serial.println(a);
       Serial.println(c);
       Serial.println(d);
       Serial.println(e);
       Serial.println(f);
       Serial.println(v);
      
       WiFiClient client;
    const int httpPort = 80;
    if (!client.connect(host, httpPort)) {
        Serial.println("connection failed");
        return;
    }
 
    // We now create a URI for the request
    String url = "/update";
    url += "?api_key=";
    url += APIKey;
    url += "&field1=";
    url += String(a);
    url += "&field2=";
    url += String(c);
    url += "&field3=";
    url += String(d);
    url += "&field4=";
    url += String(e);
    url += "&field5=";
    url += String(f);
 
 
    Serial.print("Requesting URL: ");
    Serial.println(url);
 
    // This will send the request to the server
    client.print(String("GET ") + url + " HTTP/1.1\r\n" +
                 "Host: " + host + "\r\n" +
                 "Connection: close\r\n\r\n");
    unsigned long timeout = millis();
    while (client.available() == 0) {
        if (millis() - timeout > 5000) {
            Serial.println(">>> Client Timeout !");
            client.stop();
            return;
        }
    }
 
    // Read all the lines of the reply from server and print them to Serial
    while(client.available()) {
        String line = client.readStringUntil('\r');
        Serial.print(line);
    }
 
    Serial.println();
    Serial.println("closing connection");
          

          error = deserializeJson(root,nodemcu);

          if (Firebase.setInt(firebaseData, "/Concentration/Gas/H2S", e)) {    // On successful Write operation, function returns 1  
               Serial.println("Value Uploaded Successfully H2s");
          }
          delay(2000);
           if (Firebase.setInt(firebaseData, "/Concentration/Gas/CO", a)) {    // On successful Write operation, function returns 1  
               Serial.println("Value Uploaded Successfully Co");
          }
          delay(2000);
           if (Firebase.setInt(firebaseData, "/Concentration/Gas/CH4", d)) {    // On successful Write operation, function returns 1  
               Serial.println("Value Uploaded Successfully Co2");
          }
          delay(2000);
         
            if (Firebase.setInt(firebaseData, "/Concentration/Gas/CH3", f)) {    // On successful Write operation, function returns 1  
               Serial.println("Value Uploaded Successfully CH3");
             }
              delay(2000);
             

   
          
          
          
           
  delay(2000);   
  delay(10000);
}
