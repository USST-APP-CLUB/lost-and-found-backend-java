# 这里其实用的不太对
1. 这里本来想借鉴MyBatis-Plus 代码生成器的那个感觉
2. 但是由于不到位，没有做成。
3. 此处的baseService 其实就功能来讲，应该分在dao层。service层应该另外封装接口。
4. 可以参考mybatis-plus 的BaseMapper接口，IService接口和 ServiceImpl实现类做改进。