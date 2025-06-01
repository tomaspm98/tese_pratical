method SplitArrayTest(){
  var a1:= new int[] [1,1,2,3,4,4,5,1];
  var e1: seq<int> := [1,1,2];
  var e2: seq<int> := [3, 4, 4, 5, 1];
  var res1,res2:=SplitArray(a1,3);
  PrintSplitArray(res1,res2);
  //expected [[1, 1, 2],[3, 4, 4, 5, 1]]

  var a2:= new int[] [1,1,2,3,4,4,5,1];
  var e3: seq<int> := [1,1,2,3];
  var e4: seq<int> := [4, 4, 5, 1];
  var res3,res4:=SplitArray(a2,4);
  PrintSplitArray(res3,res4);
  //expected [[1, 1, 2, 3],[4, 4, 5, 1]]

  var a3:= new int[] [1,1,2,3,4,4,5,1];
  var e5: seq<int> := [1,1];
  var e6: seq<int> := [2,3,4, 4, 5, 1];
  var res5,res6:=SplitArray(a3,2);
  PrintSplitArray(res5,res6);
  //expected [[1, 1],[2, 3, 4, 4, 5, 1]]

}