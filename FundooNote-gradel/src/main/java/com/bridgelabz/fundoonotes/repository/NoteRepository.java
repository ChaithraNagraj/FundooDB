package com.bridgelabz.fundoonotes.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.model.NoteModel;

@Repository
@Transactional
public interface NoteRepository extends JpaRepository<NoteModel, Long> {
	
	@Query(value = "select * from note where id = :id", nativeQuery = true)
	NoteModel findById(long id);
	
	@Query(value = "select * from note where user_id = :userid", nativeQuery = true)
	NoteModel findByuserid(long userid);

	@Modifying
	@Query(value = "insert into note (content, created_at,  title, updated_at, user_id, note_color, is_archived, is_deleted, is_pinned, reminder, reminder_status) values ( :content, :createdAt, :title, :updatedAt, :id, :color, :isarchived, :isdeleted, :ispinned, :reminder, :reminderstatus)" , nativeQuery = true)
	public void insertData(String content, Date createdAt, String title, Date updatedAt, long id, String color, boolean isarchived, boolean isdeleted, boolean ispinned, String reminder, boolean reminderstatus);

	@Modifying
	@Query(value = "update note set is_deleted = :b where user_id = :userid AND id = :id" ,  nativeQuery = true)
	public int delete(boolean b,long userid,long id);

	@Modifying
	@Query(value = "delete from note  where user_id = :userId AND id = :id", nativeQuery = true)
	void deleteForever(long userId, long id);

	@Modifying
	@Query(value = "update note set content = :content , title = :title , updated_at = :updatedAt where user_id = :id AND id = :id2", nativeQuery = true)
	void updateData(String content, String title, Date updatedAt, long id, long id2);

	@Modifying
	@Query(value = "update note set is_pinned = :b where user_id = :userid AND id = :id", nativeQuery = true)
	void setPinned(boolean b, long userid, long id);

	@Modifying
	@Query(value = "update note set is_archived = :b where user_id = :userid AND id = :id", nativeQuery = true)
	void setArchive(boolean b, long userid, long id);

	@Query(value = "select * from note where user_id = :userId", nativeQuery = true)
	List<NoteModel> getAll(Long userId);

	@Modifying
	@Query(value = "delete from note where user_id = :userId and is_deleted = true", nativeQuery = true)
	void empty(long userId);

	@Modifying
	@Query(value = "update note set note_color = :color where user_id = :userid and id = :id", nativeQuery = true)
	void updateColor(long userid, long id, String color);

	@Query(value = "select * from note where user_id=:userId and id=:noteId", nativeQuery = true)
	List<NoteModel> searchAllNotesByNoteId(long userId, Long noteId);

	@Query(value = "select * from note where user_id = :userId and is_pinned = true and is_deleted = false and is_archived = false", nativeQuery = true)
	List<NoteModel> getallpinned(Long userId);

	@Query(value = "select * from note where user_id = :userId and is_pinned = false and is_deleted = false and is_archived = false", nativeQuery = true)
	List<NoteModel> getallunpinned(Long userId);

	@Query(value = "select * from note where user_id = :userId and is_archived = true and is_deleted = false", nativeQuery = true)
	List<NoteModel> getallarchived(Long userId);

	@Query(value = "select * from note where user_id = :userId and is_archived = false", nativeQuery = true)
	List<NoteModel> getallunarchived(Long userId);

	@Query(value = "select * from note where user_id = :userId and is_deleted = true", nativeQuery = true)
	List<NoteModel> getalltrashed(Long userId);
	
	@Query(value = "select * from note where user_id = :userId and reminder_status = true", nativeQuery = true)
	List<NoteModel> getallreminder(Long userId);
	
}
