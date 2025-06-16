method IsMinHeapTest(){
  var a1:= new int[] [1, 2, 3, 4, 5, 6];
  var res1:=IsMinHeap(a1);
  print(res1);print("\n");
              //assert res1==true;

  var a2:= new int[] [2, 3, 4, 5, 10, 15];
  var res2:=IsMinHeap(a2);
  print(res2);print("\n");
              //assert res2==true;

  var a3:= new int[] [2, 10, 4, 5, 3, 15];
  var res3:=IsMinHeap(a3);
  print(res3);print("\n");
              //assert res3==false;

}