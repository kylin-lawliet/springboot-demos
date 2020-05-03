import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * <p> 描述 ：记录sql xml的一些写法
 * @author : blackcat
 * @date : 2020/3/11 13:52
 */
@SpringBootTest
public class SqlXmlTest {

    /**
     * <p> 描述 : 一对多 和 父级
     * @author : blackcat
     * @date  : 2020/3/11 13:56
    */
    @Test
    private void test(){
        /*public class BlogCode{
            ......
            @Transient
            @TableField(exist = false)
            private BlogCode parent;
            @Transient
            @TableField(exist = false)
            private List<BlogCode> nodes;
        }*/

    /*<resultMap id="BaseResultMap" type="com.blackcat.blog.core.entity.BlogCode">
        <id column="id" property="id" />
        <result column="name" property="name" />
        <result column="parent_id" property="parentId" />
        <result column="code_id" property="codeId" />
        <result column="sort" property="sort" />
        <result column="remarks" property="remarks" />
        <result column="create_time" property="createTime" />
        <result column="update_time" property="updateTime" />
        父级关联
        <association property="parent" javaType="com.blackcat.blog.core.entity.BlogCode">
            <id column="id" property="id" />
            <result column="name" property="name" />
            <result column="parent_id" property="parentId" />
            <result column="remarks" property="remarks" />
        </association>
        一对多关联
        <collection property="nodes" column="node_id" javaType="ArrayList" ofType="com.blackcat.blog.core.entity.BlogCode">
            <id column="node_id" property="id" />
            <result column="node_name" property="name" />
            <result column="node_parent_id" property="parentId" />
            <result column="node_remarks" property="remarks" />
        </collection>
    </resultMap>*/
    /*<select id="listCodeByType" resultMap="BaseResultMap" parameterType="java.lang.String">
            SELECT
            bc.id,
            bc.`name`,
            bc.parent_id,
            bc.remarks,
            node.id AS node_id,
            node.`name` AS node_name,
            node.parent_id AS node_parent_id,
                    node.remarks AS node_remarks
            FROM
            blog_code bc
            LEFT JOIN blog_code node ON (
                    node.parent_id = bc.id
            )
            WHERE
            bc.parent_id is null
            and
            bc.code_id=#{codeId}
            ORDER BY
            bc.sort ASC,
            node.sort ASC
	</select>*/

    }
}
