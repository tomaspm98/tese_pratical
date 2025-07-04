method DeepCopySeqTest(){
  var s1: seq<int> := [5, 2, 3, 3];
  var e1: seq<int> := [5, 2, 3, 3];
  var res1:=DeepCopySeq(s1);
  print(res1);print("\n");
              //expected [5, 2, 3, 3]


  var s2: seq<int> := [3,4,7,2,6,9];
  var e2: seq<int> := [3,4,7,2,6,9];
  var res2:=DeepCopySeq(s2);
  print(res2);print("\n");
              //expected [3,4,7,2,6,9]


  var s3: seq<int> := [6,8,3,5,7,3,5,87];
  var e3: seq<int> := [6,8,3,5,7,3,5,87];
  var res3:=DeepCopySeq(s3);
  print(res3);print("\n");
              //expected [6,8,3,5,7,3,5,87]
}