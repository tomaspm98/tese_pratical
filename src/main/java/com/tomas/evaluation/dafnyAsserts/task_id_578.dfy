method InterleaveTest(){
  var s1: seq<int> := [1,2,3,4,5,6,7];
  var s2: seq<int> := [10,20,30,40,50,60,70];
  var s3: seq<int> := [100,200,300,400,500,600,700];
  var res1:=Interleave(s1,s2,s3);
  print(res1);print("\n");
              //expected [1, 10, 100, 2, 20, 200, 3, 30, 300, 4, 40, 400, 5, 50, 500, 6, 60, 600, 7, 70, 700];



  var s4: seq<int> := [10,20];
  var s5: seq<int> := [15,2];
  var s6: seq<int> := [5,10];
  var res2:=Interleave(s4,s5,s6);
  print(res2);print("\n");
              //expected [10,15,5,20,2,10];



  var s7: seq<int> := [11,44];
  var s8: seq<int> := [10,15];
  var s9: seq<int> := [20,5];
  var res3:=Interleave(s7,s8,s9);
  print(res3);print("\n");
              //expected [11,10,20,44,15,5];

}