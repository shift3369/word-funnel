# wordfunnel
텍스트 파일을 읽어 원하는 라인별로 단어의 유효성을 검사하고
원하는 파티션 수 만큼 나누어 병렬적으로 처리하여 대상 파일에 쓸수 있는 어플리케이션입니다

## How to use
Program arguments에 다음과 같은 순서로 파라미터를 입력합니다.
처리해야할 파일명 , 결과 파일을 저장할 경로, 파티션 (1 < N < 28)

   > words.txt result 3
   
   > words.txt ./path/result 5
  
## Class Architecture
+ WordFunnel
    + Main 클래스
+ WordProducer
    + 입력 파일에서 단어를 읽어 유효한 단어라면 MessageBroker에게 전달합니다.
+ WordConsumer
    + MessageBroker에서 파티션을 assign 받아 해당 파티션의 메세지들을 소비합니다.
    + DuplicationValidator 를 아용하여 히스토리 검사 후 대상 파일에 write 합니다.
+ MessageBroker
    + 입력한 수만큼의 파티션을 초기화 합니다.
    + producer 가 생산한 메세지의 hash 값을 이용하여 파티션을 분배하여줍니다.
    + 컨슈머에게 파티션을 할당해줍니다.
+ PartitionDistributor
    + 파티션 수와, 단어를 Standardize 하여 hash 값을 구한뒤 파티션 인덱스를 반환합니다.
+ WordValidator
    + 정규식을 이용하여 파일에서 읽은 단어의 유효성을 검사합니다.
+ DuplicationValidator
    + FileSyncWriter가 파일에 쓸때, 히스토리를 검색하고 저장한 후 유효성 체크 결과를 반환합니다.


## TO DO
+ 현재는 파티션 수와 동일하게 consumer를 생성하였지만, consumer의 수를 사용자가 설정 가능하게 하고,
 MessageBroker 에게 할당받을 수 있도록 해야한다. 이때 Arbitration, Starvation에 대한 정책을 마련해야 합니다.
+ 현재 producer 에서 파일의 끝을 읽은 경우 Eof에 대한 메세지를 보냄으로써, MessageBroker 와 consumer 에 대한 동작을 
제어하지만 추후 리소스 managing 역힐을 하는 클래스에 대한 고민이 필요합니다.
+ 현재 단어의 write 히스토리를 메모리에서 가지고 있지만 추후 다른 저장소를 이용하여 히스토리 관리가 필요합니다.
 

    
    
