method InsertBeforeEachTest(){
  var s1: seq<string> := ["Red", "Green", "Black"];
  var res1:=InsertBeforeEach(s1,"c");
  PrintSeq(res1);print("\n");
                 //expected ["c", "Red", "c", "Green", "c", "Black"];


  var s2: seq<string> := ["python", "java"];
  var res2:=InsertBeforeEach(s2,"program");
  PrintSeq(res2);print("\n");
                 // expected ["program", "python", "program", "java"];

  var s3: seq<string> := ["happy", "sad"];
  var res3:=InsertBeforeEach(s3,"laugh");
  PrintSeq(res3);print("\n");
                 // expected ["laugh", "happy", "laugh", "sad"];

}