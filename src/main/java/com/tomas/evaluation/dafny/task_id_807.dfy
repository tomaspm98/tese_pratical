predicate IsOdd(x: int)
{
    x % 2 != 0
}

method FindFirstOdd(a: array<int>) returns (found: bool, value: int)
    requires a != null
    ensures !found ==> forall i :: 0 <= i < a.Length ==> !IsOdd(a[i])
    ensures found ==> IsOdd(value) && exists i :: 0 <= i < a.Length && a[i] == value && forall j :: 0 <= j < i ==> !IsOdd(a[j])
{
    var index := 0;
    found := false;
    value := 0;

    while (index < a.Length)
        invariant 0 <= index <= a.Length
        invariant !found ==> forall i :: 0 <= i < index ==> !IsOdd(a[i])
        invariant found ==> IsOdd(a[index - 1]) && value == a[index - 1] && forall i :: 0 <= i < index - 1 ==> !IsOdd(a[i])
    {
        if IsOdd(a[index])
        {
            found := true;
            value := a[index];
            return;
        }
        index := index + 1;
    }
}
