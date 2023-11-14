# kotlin-diet-memo
[Kotlin] 다이어트 메모를 기록하는 `안드로이드` 앱

<br>

## 🎥 기능

_아래의 GIF는 2개의 다른 디바이스에서 테스트하였습니다._

`Large Device`

![phone1](https://github.com/1three/kotlin-diet-memo/assets/94810322/2025f12e-84df-4e59-917a-56332c9df09e)

`7 Pro Device`

![phone2](https://github.com/1three/kotlin-diet-memo/assets/94810322/b42a817b-1699-4571-9206-c01c55359750)

```
1. 로그인 없이 휴대폰의 토큰으로 개인을 식별합니다.
2. 내가 기록한 메모만 볼 수 있습니다.
3. 날짜를 선택할 수도, 선택하지 않을 수도 있습니다.
```

<br>

## 👩🏻‍🔬 배운 점

#### 🧪 다이얼로그

_**다이얼로그**_ 란, 사용자와 상호작용하며 정보를 요청하거나 알림을 표시하는 작은 팝업 창입니다.

```
- LayoutInflater.from(this).inflate
  다이얼로그 layout 인플레이트

- AlertDialog.Builder
  다이얼로그 생성

- show()
  다이얼로그 보이기

- setView
  다이얼로그에 표시할 커스텀 뷰를 설정
  인플레이트한 뷰를 다이얼로그에 설정해 내용을 표시

- setTitle
  다이얼로그 제목 설정
```

<br>

```Kotlin
val writeBtn: ImageView = findViewById(R.id.writeBtn)

writeBtn.setOnClickListener {
    val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)

    val mBuilder = AlertDialog.Builder(this)
        .setView(mDialogView)
        .setTitle("운동 메모")

    val mAlertDialog = mBuilder.show()
}
```

<br>

#### 🧪 날짜를 선택할 수 있는 버튼 만들기

DatePickerDialog를 통해 날짜를 선택할 수 있는 버튼을 만들 수 있습니다.

```
- GregorianCalendar().get(Calendar.YEAR)
  현재 날짜 가져오기
  YEAR, MONTH, DATE 등을 통해 년도, 월, 일자를 가져올 수 있습니다.

- DatePickerDialog
  날짜 선택을 위한 다이얼로그

- DatePickerDialog.OnDateSetListener
  날짜가 선택되었을 때의 동작을 정의
```

<br>

```Kotlin
val dateSelectBtn = mAlertDialog.findViewById<Button>(R.id.dateSelectBtn)

dateSelectBtn?.setOnClickListener {
    val today = GregorianCalendar()
    val year: Int = today.get(Calendar.YEAR)
    val month: Int = today.get(Calendar.MONTH)
    val date: Int = today.get(Calendar.DATE)

    val dig = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            dateSelectBtn.setText("${year}/${month + 1}/${dayOfMonth}")
        }
    }, year, month, date)

    dig.show()
}
```

<br>

#### 🧪 push()로 데이터 저장하기

push()을 제외하니 DB에 단 1개의 값만 저장된다는 것을 아시나요...? _(저도 알고 싶지 않았어요...)_

Firebase Realtime Database에 데이터를 저장할 때, **push()** 메서드를 사용하면<br>
자동으로 고유한 키를 생성하여 데이터를 저장합니다.

이를 통해 데이터의 중복을 방지하고, 새로운 데이터를 추가할 수 있습니다.

```Kotlin
myRef.push().setValue(model)
```

<br>

#### 🧪 데이터를 읽어오기 전, clear()를 사용하는 이유

데이터를 읽어오기 전에 기존 데이터를 초기화하기 위해 clear() 메서드를 사용합니다.<br>
이를 통해 이전 데이터의 영향을 받지 않고 **새로운 데이터를 로드**할 수 있습니다.

```Kotlin
myRef.child(Firebase.auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) {
        // 기존 데이터를 초기화
        dataModelList.clear()

        for (dataModel in snapshot.children) {
            dataModelList.add(dataModel.getValue(DataModel::class.java)!!)
        }
        ...
```

<br>

#### 🧪 DB에서 데이터를 읽어올 때에는 for문 사용하기

snapshot.children는 데이터베이스의 모든 하위 항목을 나타내는 DataSnapshot의 컬렉션입니다.<br>
이를 순회하면서 각각의 데이터를 가져와서 데이터 모델 리스트에 추가할 수 있습니다.

```Kotlin
// DB에서 가져온 스냅샷 순회 및 데이터를 가져와 데이터 모델 리스트에 추가
for (dataModel in snapshot.children) {
    dataModelList.add(dataModel.getValue(DataModel::class.java)!!)
}
```

<br>

#### 🧪 변경된 데이터를 화면에 표시하기 위한 notifyDataSetChanged()

notifyDataSetChanged()는 데이터 변경을 알려 어댑터에게 새로운 데이터가 반영되었음을 알립니다.<br>
이를 호출하여 어댑터의 **리스트뷰를 업데이트**하고, _변경된 데이터를 화면에 표시할_ 수 있습니다.

```Kotlin
listViewAdapter.notifyDataSetChanged()
```
