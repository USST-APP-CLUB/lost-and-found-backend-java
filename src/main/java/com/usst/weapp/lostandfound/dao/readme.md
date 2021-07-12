## mapper层
mappper层和所谓的dao层属于同一层。

虽然目前使用的是mongodb（nosql）

但不排除以后使用mysql等关系型数据库的可能。为了便于扩展mybatis框架，所以定义成mapper层

当然不用mapper层也可以，在引入mybatis时可能需要做其他配置。但为了方便起见，用mapper较好