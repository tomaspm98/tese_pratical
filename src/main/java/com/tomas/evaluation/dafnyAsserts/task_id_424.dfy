method ExtractRearCharsTest(){
  var s1: seq<string> := ["Mers", "for", "Vers"];
  var res1:=ExtractRearChars(s1);
  print(res1);print("\n");
              //expeced ['s', 'r', 's']

  var s2: seq<string> := ["Avenge", "for", "People"];
  var res2:=ExtractRearChars(s2);
  print(res2);print("\n");
              //expeced ['e', 'r', 'e']

  var s3: seq<string> := ["Gotta", "get", "go"];
  var res3:=ExtractRearChars(s3);
  print(res3);print("\n");
              //expeced ['a', 't', 'o']

}