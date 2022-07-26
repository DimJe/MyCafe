# My Cafe
 - 개인프로젝트
 - <a href="https://play.google.com/store/apps/details?id=com.Dimje.mymap" target="_blank">플레이스토어 출시 링크</a>
## 기능
 - 네이버 map api와 카카오 검색 api를 사용한 내 주위 카페를 map에 띄우고 firebase를 사용하여 카페에 리뷰 작성까지 할 수 있는 어플리케이션
## 사용한 기술 및 라이브러리
 - Retrofit
 - liveData
 - RecyclerView
 - firebase
 - viewpager
## 작동화면
<img src="https://user-images.githubusercontent.com/41900899/180183117-20e67f2a-df3a-4ad5-8fe5-ef9d242d5bff.jpg" width = 130/><img src="https://user-images.githubusercontent.com/41900899/180184857-d2f3cf5b-4055-4e6d-b92b-14ec6cbd70b0.jpg" width = 130/><img src="https://user-images.githubusercontent.com/41900899/180185006-689a4baa-9877-4e31-9117-502132b0f5f0.jpg" width = 130/>
    <img src="https://user-images.githubusercontent.com/41900899/180185730-3d4935a1-1c97-4348-9a31-a47f68da24c4.jpg" width = 130>
    <img src="https://user-images.githubusercontent.com/41900899/180185739-d6c176f7-ba70-4857-8b3f-f68ec57cc4d4.jpg" width = 130>
    <img src="https://user-images.githubusercontent.com/41900899/180185737-57293ae5-c7a5-4c7c-8144-74ace9d02186.jpg" width = 130>
## 구현
 -  ### 1.MainActivity
    네이버 맵을 보여주는 Activity.<br>
    기본 설정은 네이버 공식문서를 참고하여 진행하였다.
    카카오 api를 통해 얻은 카페 정보들은 APIViewModel으로 이름만 지어둔 클래스에서 Livedata를 통해 관리하고 있다. 지도에 마커를 띄우는 코드는 MainActivity.kt의 show() 통해 진행된다. 
    <details>
    <summary>show()코드 보기</summary>
    <div markdown = "1">

    ```java
        private fun show(result: List<Document>) {
        for (cafeInfo in result) {
            Log.d(TAG, "showCafe: ${cafeInfo.address_name}")
            val marker = Marker()
            marker.icon = MarkerIcons.BLACK
            marker.iconTintColor = Color.BLUE

            val infoWindow = InfoWindow()
            infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(this) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return cafeInfo.place_name
                }
            }
            marker.setOnClickListener {
                val intent = Intent(this, ReviewActivity::class.java)
                intent.putExtra("position", result.indexOf(cafeInfo))
                startActivity(intent)
                false
            }
            marker.position = LatLng(cafeInfo.y.toDouble(), cafeInfo.x.toDouble())
            marker.map = naverMap
            markerList.add(marker)
            infoWindow.open(marker)
        }
    }
    ```
    </div>
    </details>
  - ### 2.CafeListActivity
    지도로 보던 카페들의 위치를 리스트로 보여주는 Activity.<br>
    API로 얻어 온 카페들의 정보들을 가지고 RecyclerView를 통해 구현했다.

  - ### 3.ReviewActivty
    지도에서 마커를 클릭하거나 리스트에서 카페목록 중 하나를 클릭하면 넘어가는 리뷰 Activity.<br>
    지도에 표시하거나 리스트로 표현하는 행동들은 모두 APIViewModel이 가지고 있는 livedata에서 이루어지기에 액티비티 전환간에 데이터 전달은 해당 데이터의 인덱스 전달하면서 이루어진다. 넘어온 인덱스로 클릭한 카페의 이름을 key로 firebase에 접근하여 해당 카페의 리뷰들을 읽어온다.

    ```java
    dbModel.getData(position)
        dbModel.reviewList.observe(this,{
            reAdapter.submitList(it)
            reviewRecyclerView.adapter = reAdapter
            reviewRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
            cafePoint.text = if (dbModel.count==0) "맛있나요? : 0  이쁜가요? : 0  공부하기 좋은가요? : 0"
                             else "맛있나요? : ${((dbModel.taste / dbModel.count)*10).roundToInt()/10f}  " +
                                  "이쁜가요? : ${((dbModel.beauty/ dbModel.count)*10).roundToInt()/10f}  " +
                                  "공부하기 좋은가요? : ${((dbModel.study/ dbModel.count)*10).roundToInt()/10f}"
        })
    ```

  - ### 4.API 호출
    API요청의 응답으로 얻는 데이터 형식은 아래와 같다.
    ```java
    data class Cafeinfo(
    val documents: List<Document>,
    val meta : Meta
    )
    data class Document(
        val address_name: String,
        val category_group_code: String,
        val category_group_name: String,
        val category_name: String,
        val distance: String,
        val id: String,
        val phone: String,
        val place_name: String,
        val place_url: String,
        val road_address_name: String,
        val x: String,
        val y: String
    )
    data class Meta(
        val same_name: Same_name,
        val is_end : Boolean
    )
    data class Same_name(
        val keyword : String
    )
    ```
    APIViewModel 클래스에서 응답으로 온 데이터들을 MutableLivedata로 관리하며
    APIViewModel 클래스를 싱글톤으로 생성하여 관리한다.
    <details>
    <summary>APIViewModel 코드 보기</summary>
    <div markdown = "1">

    ```java
    class APIViewModel : ViewModel(){

        var result : MutableLiveData<Cafeinfo> = MutableLiveData()
        var retrofit : Retrofit
        var BASE_URL_KAKAO_API : String
        var REST_API_KEY : String
        init {
            Log.d(TAG, "object: created")
            BASE_URL_KAKAO_API = "https://dapi.kakao.com/"
            REST_API_KEY = "KakaoAK b6687296c27e98184bd039bd2e288f48"
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_KAKAO_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        fun loadCafe(name:String) {
            val api = retrofit.create(SearchCafeService::class.java)

            val callGetSearchCafe = api.getSearchCafe(REST_API_KEY,
                locationOverlay.position.longitude,
                locationOverlay.position.latitude,"CE7",2100,name)
            callGetSearchCafe.enqueue(object : Callback<Cafeinfo> {
                override fun onResponse(call: Call<Cafeinfo>, response: Response<Cafeinfo>) {
                    Log.d(TAG,"CallAPI - onResponse() called")
                    result.value = response.body()
                }
                override fun onFailure(call: Call<Cafeinfo>, t: Throwable) {
                    Log.d(TAG,"CallAPI - onFailure() called ${t.localizedMessage}")
                }
            })
        }
        fun loadCafe_other(){
            val api = retrofit.create(SearchOtherService::class.java)
            val callGetSearchOther = api.getSearchOther(REST_API_KEY,
                locationOverlay.position.longitude,
                locationOverlay.position.latitude,"CE7",1000)
            callGetSearchOther.enqueue(object : Callback<Cafeinfo> {
                override fun onResponse(call: Call<Cafeinfo>, response: Response<Cafeinfo>) {
                    Log.d(TAG,"MainActivity - onResponse() called")
                    result.value = response.body()
                }
                override fun onFailure(call: Call<Cafeinfo>, t: Throwable) {
                    Log.d(TAG,"MainActivity - onFailure() called ${t.localizedMessage}")
                }
            })
        }
    }
    ```
    </div>
    </details>
    <br>
  - ### 5.Firebase 접근
    firebase에서는 카페 이름을 key로 사용하여 카페들의 리뷰를 저장하고 있다.<br>
    데이터베이스에 데이터를 삽입하거나 데이터를 얻어 오는 과정도 DBViewModel 클래스를 생성하여 livedata로 진행한다.
    Review 클래스는 다음과 같다.
    
    ```java
    data class Review(val review : String="",
                      val taste : String="",
                      val beauty : String="",
                      val study : String="",
                      val date:String=""){}
    ```
    데이서 삽입은 사용자가 입력한 데이터로 review 클래스를 생성하고 밑의 코드를 이용하여 삽입한다.
    ```java
    mDatabase.child(model.result.value!!.documents[position].place_name).push().setValue(review)
    ```

    데이터 조회는 ValueEventListener()를 사용해서 얻어온다
    ```java
    fun getData(position: Int){
        mDatabase.child(model.result.value!!.documents[position].place_name).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: error")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                taste = 0.0
                beauty = 0.0
                study = 0.0
                count = 0
                temp.clear()
                for (data in snapshot.children){
                    val review = Review(
                        data.child("review").value.toString(),
                        data.child("taste").value.toString(),
                        data.child("beauty").value.toString(),
                        data.child("study").value.toString(),
                        data.child("date").value.toString()
                    )
                    Log.d(TAG, "onDataChange: ${review.review}")
                    taste += review.taste.toDouble()
                    beauty += review.beauty.toDouble()
                    study += review.study.toDouble()
                    count++
                    temp.add(review)
                }
                reviewList.value = temp
            }
        })
    }
    ```
## 개선점 및 느낀점
- api,firebase 등 처음 사용해보는 것들이 많아서 정확히 이해하고 사용했다고는 하지 못할 것 같다. 리팩토링을 진행하면서 다시 한번 이해하고 넘어가야겠다.
- 이름만 ViewModel로 작성했을 뿐이라 mvvm의 개념 및 예제를 통해 익히고 이 프로젝트를 리팩토링 하고 싶다.
