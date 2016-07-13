JSON Object can be created in java with the help of the below Jars/ Packages

1. java-json.jar
 2. json-simple-1.1.1.jar
 3. javax.json.jar OR by importing javax.json.* in Java 7 or above.

Now let use see an example of JSON  and how to create the same in java using java.json jar. And also let us see how to read and parse the same Json file using Java.

Before continuing to the code, let us see  the format of JSON.

JavaScript Object Notation (JSON) is a text-based data interchange format, an alternative to XML. JSON Object structure can be either as key-value pairs or ordered list of values.

Example JSON:

{“Employees”:[

{
“Name”:”ABC”,
“Designation”:”Manager”,
“Pay”:”Rs. 60000/-“,
“PhoneNumbers”:[{
“LandLine” : “11-2xxxx99”,
“Mobile” : “11xxxxxx11”
}]
 },

{
“Name”:”XYZ”,
“Designation”:”Sr. Manager”,
“Pay”:”Rs. 70000/-“,
“PhoneNumbers”:[{
“LandLine” : “11-2xxxx32”,
“Mobile” : “66xxxxxx66”
}]
 }

]}
In JSON, [] represents Array. {} represents Object.

Java Code for writing and reading JSON file. 



1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
63
64
65
66
67
68
69
70
71
72
73
74
75
76
77
78
79
80
81
82
83
84
85
86
87
88
89
90
91
92
93
94
95
96
97
98
99
100
101
102
103
104
105
106
107
108
109
110
111
112
113
114
115
116
117
118
119
120
121
122
123
124
125
126
127
128
129
 
package dashboard;
 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
public class JSonFileWrite1 {
 
public static void main (String args[]) throws JSONException
{
String fileName="c:/temp/test.json";
jsonFileWrite(fileName);
jsonFileRead(fileName);
}
 
//writing JSON
public static void jsonFileWrite(String fileName) throws JSONException
{
 
JSONObject allEmps=new JSONObject();
 
JSONArray empArray=new JSONArray();
JSONObject empObj;
JSONArray phoneNumbers =null;
JSONObject phoneObj;
 
empObj = new JSONObject();
empObj.put("Name", "ABC");
empObj.put("Designation", "Manager");
empObj.put("Pay", "Rs. 60000/-");
 
phoneNumbers= new JSONArray();
phoneObj=new JSONObject();
phoneObj.put("LandLine", "11-2xxxx99");
phoneObj.put("Mobile", "99xxxxxx11");
phoneNumbers.put(phoneObj);
 
empObj.put("PhoneNumbers", phoneNumbers);
 
empArray.put(empObj);
 
empObj = new JSONObject();
empObj.put("Name", "ABC");
empObj.put("Designation", "Sr.Manager");
empObj.put("Pay", "Rs. 60000/-");
 
phoneNumbers= new JSONArray();
phoneObj=new JSONObject();
phoneObj.put("LandLine", "12-2xxxx129");
phoneObj.put("Mobile", "45xxxxxx11");
phoneNumbers.put(phoneObj);
 
empObj.put("PhoneNumbers", phoneNumbers);
 
empArray.put(empObj);
 
allEmps.put("Employees", empArray);
 
try {
 
// Writing to a file
FileWriter file = new FileWriter(fileName);
file.write(allEmps.toString());
file.flush();
file.close();
 
System.out.println("JSon file created");
} catch (IOException e) {
e.printStackTrace();
}
 
}
 
/*The content of the created test.json file is given below.
 
{"Employees":[{"Designation":"Manager","PhoneNumbers":[{"Mobile":"99xxxxxx11","LandLine":"11-2xxxx99"}],"Pay":"Rs. 60000/-","Name":"ABC"},{"Designation":"Sr.Manager","PhoneNumbers":[{"Mobile":"45xxxxxx11","LandLine":"12-2xxxx129"}],"Pay":"Rs. 60000/-","Name":"ABC"}]} */
 
&nbsp;
 
//reading JSON
 
public static void jsonFileRead(String fileName) throws JSONException
{
 
try {
 
Scanner scanner = new Scanner( new File(fileName) );
String jsonString = scanner.useDelimiter("\\A").next();
scanner.close();
System.out.println("jsonString =" + jsonString);
JSONObject empObj = new JSONObject();
JSONObject phoneObj = new JSONObject();
 
JSONObject jsonObject = new JSONObject(jsonString);
 
JSONArray jsonArray= jsonObject.getJSONArray("Employees");
 
for (int j=0; j<jsonArray.length();j++)
{
empObj =jsonArray.getJSONObject(j);
String nameOfCountry = (String) empObj.get("Name");
System.out.println("Name : "+nameOfCountry);
 
String population = (String) empObj.get("Designation");
System.out.println("Designation: "+population);
 
System.out.println("Phone Numbers are :");
JSONArray phoneNumbers = (JSONArray) empObj.get("PhoneNumbers");
for (int i=0; i<phoneNumbers.length();i++)
{
phoneObj=phoneNumbers.getJSONObject(i);
System.out.println(" Land Line " + phoneObj.get("LandLine"));
System.out.println(" Mobile " + phoneObj.get("Mobile"));
}
 
}
 
} catch (FileNotFoundException e) {
e.printStackTrace();
}
 
}
 
}
 
