method PowerOfListElementsTest(){
  var s1: seq<int> :=[1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  var res1:=PowerOfListElements(s1,2);
  print(res1);print("\n");
              //expected [1, 4, 9, 16, 25, 36, 49, 64, 81, 100]

  var s2: seq<int> :=[10,20,30];
  var res2:=PowerOfListElements(s2,3);
  print(res2);print("\n");
              //expected [1000, 8000, 27000]

  var s3: seq<int> :=[12,15];
  var res3:=PowerOfListElements(s3,5);
  print(res3);print("\n");
              //expected [248832, 759375]


}