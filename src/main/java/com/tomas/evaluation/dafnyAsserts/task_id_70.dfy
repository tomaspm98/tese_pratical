    var s1:seq<seq<int>> :=[[11, 22, 33], [44, 55, 66]];
    var res1:=AllSequencesEqualLength(s1);
    expect res1==true;

    var s2:seq<seq<int>> :=[[1, 2, 3], [4, 5, 6, 7]];
    var res2:=AllSequencesEqualLength(s2);
    expect res2==false;

    var s3:seq<seq<int>> :=[[1, 2], [3, 4]];
    var res3:=AllSequencesEqualLength(s3);
    expect res3==true;
}