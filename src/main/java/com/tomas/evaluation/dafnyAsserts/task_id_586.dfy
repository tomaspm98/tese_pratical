method SplitAndAppendTest(){
  var l1: seq<int> := [12,10,5,6,52,36];
  var res1:=SplitAndAppend(l1,2);
  print(res1);print("\n");
              //expected [5,6,52,36,12,10]

  var l2: seq<int> := [1,2,3,4];
  var res2:=SplitAndAppend(l2,1);
  print(res2);print("\n");
              //expected [2,3,4,1]

  var l3: seq<int> := [0,1,2,3,4,5,6,7];
  var res3:=SplitAndAppend(l3,3);
  print(res3);print("\n");
              //expected [3,4,5,6,7,0,1,2]

}