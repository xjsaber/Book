package com.xjsaber.learn.spring.springboot.dao;

import com.xjsaber.learn.spring.springboot.pojo.JpaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author xjsaber
 */
public interface JpaUserRepository
    extends JpaRepository<JpaUser, Long> {

    /**
     * ���û�����ģ����ѯ
     * @param userName �û���
     * @return �û��б�
     */
    @Query("from userJpa where user_name like concat('%', ?1, '%') ")
    List<JpaUser> findByUserNameLike(String userName);

    /**
     * ����������ѯ
     * @param id ��������
     * @return �û�
     */
    JpaUser getUserById(Long id);

    /**
     * �����û����ƻ��߱�ע����ģ����ѯ
     * @param userName �û���
     * @param note ��ע
     * @return �û��б�
     */
    @Query("from userJpa where user_name like concat('%', ?1, '%') "
            + "and note like concat('', ?2, '%')")
    List<JpaUser> findByUserNameLikeOrNoteLike(String userName, String note);
}
