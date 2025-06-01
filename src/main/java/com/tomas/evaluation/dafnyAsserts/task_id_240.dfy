method ReplaceLastElementTest(){
  var a1:seq<int> := [1, 3, 5, 7, 9, 10];
  var a2:seq<int> := [2, 4, 6, 8];
  var res1:=ReplaceLastElement(a1,a2);
  expect res1==[1, 3, 5, 7, 9, 2, 4, 6, 8];

  var a3:seq<int> := [1,2,3,4,5];
  var a4:seq<int> := [5,6,7,8];
  var res2:=ReplaceLastElement(a3,a4);
  expect res2==[1,2,3,4,5,6,7,8];

  var a5:seq<int> := [1,2,4,6,8];
  var a6:seq<int> := [3,5,7,9];
  var res3:=ReplaceLastElement(a5,a6);
  expect res3==[1, 2, 4, 6, 3, 5, 7, 9];

}