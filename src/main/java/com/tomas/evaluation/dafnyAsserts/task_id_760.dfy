method HasOnlyOneDistinctElementTest(){
  var a1:= new int[] [1,1,1];
  var res1:=HasOnlyOneDistinctElement(a1);
  expect res1==true;

  var a2:= new int[] [1,2,1,2];
  var res2:=HasOnlyOneDistinctElement(a2);
  expect res2==false;

  var a3:= new int[] [1,2,3,4,5];
  var res3:=HasOnlyOneDistinctElement(a3);
  expect res3==false;

}

