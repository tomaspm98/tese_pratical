method DissimilarElementsTest(){
  var a1:= new int[] [3, 4, 5, 6];
  var a2:= new int[] [5, 7, 4, 10];
  var res1:=DissimilarElements(a1,a2);
  expect res1==[3, 6, 7, 10];

  var a3:= new int[] [1, 2, 3, 4];
  var a4:= new int[] [7, 2, 3, 9];
  var res2:=DissimilarElements(a3,a4);
  expect res2== [1, 4, 7, 9];

  var a5:= new int[] [21, 11, 25, 26];
  var a6:= new int[] [26, 34, 21, 36];
  var res3:=DissimilarElements(a5,a6);
  expect res3==[11, 25, 34, 36];

}

