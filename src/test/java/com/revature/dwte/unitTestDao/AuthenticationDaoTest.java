package com.revature.dwte.unitTestDao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.revature.dwte.dao.AuthenticationDao;
import com.revature.dwte.model.User;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthenticationDaoTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private AuthenticationDao authDao;

	/*-	*******************************
	 * 	getUserByEmailAndPassword Tests
	 * 	*******************************
	 */
	@Test
	@Transactional
	public void testGetUserByEmailAndPassword_positive() {
		User user = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		this.entityManager.persist(user);

		this.entityManager.flush();

		// ACT
		User actual = this.authDao.getUserByEmailAndPassword("nedoe@yahoo.com", "disIsMyPassword13");

		// ASSERT
		User expected = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		expected.setUserId(1);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	@Transactional
	public void testGetUserByEmailAndPassword_incorrectPassword() {
		User user = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		this.entityManager.persist(user);

		this.entityManager.flush();

		User actual = this.authDao.getUserByEmailAndPassword("nedoe@yahoo.com", "0123456789");

		// ASSERT
		User expected = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		expected.setUserId(1);

		Assertions.assertNotEquals(expected, actual);

	}

	@Test
	@Transactional
	public void testGetUserByEmailAndPassword_incorrectEmail() {
		User user = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		this.entityManager.persist(user);

		this.entityManager.flush();

		User actual = this.authDao.getUserByEmailAndPassword("123456@yahoo.com", "disIsMyPassword13");

		// ASSERT
		User expected = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		expected.setUserId(1);

		Assertions.assertNotEquals(expected, actual);

	}

	@Test
	@Transactional
	public void testGetUserByEmailAndPassword_incorrectEmailAndPassword() {
		User user = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		this.entityManager.persist(user);

		this.entityManager.flush();

		// ACT
		User actual = this.authDao.getUserByEmailAndPassword("123456@yahoo.com", "123456789");

		// ASSERT
		User expected = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		expected.setUserId(1);

		Assertions.assertNotEquals(expected, actual);

	}

	@Test
	@Transactional
	public void testGetUserByEmailAndPassword_userDoesNotExist() {
		User user = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		this.entityManager.persist(user);

		User expected = this.authDao.getUserByEmailAndPassword("nedoe@yahoo.com", "disIsMyPassword13");

		User actual = new User();

		Assertions.assertNotEquals(expected, actual);
	}

	/*-	****************
	 * 	signUpUser Tests
	 * 	****************
	 */
	@Test
	@Transactional
	public void testSignUpUserAsAdmin_positive() {
		User user = new User("Jane", "Doe", "jane_doe@gmail.com", "Jane!123", "5712561234", "Admin");
		this.authDao.signUpUser(user);

		User actual = this.authDao.getUserByEmailAndPassword("jane_doe@gmail.com", "Jane!123");

		User expected = new User("Jane", "Doe", "jane_doe@gmail.com", "Jane!123", "5712561234", "Admin");
		expected.setUserId(1);

		Assertions.assertEquals(expected, actual);

		this.entityManager.flush();
	}

	@Test
	@Transactional
	public void testSignUpUserAsMember_positive() {
		User user = new User("Jane", "Doe", "jane_doe@gmail.com", "Jane!123", "5712561234", "Member");
		this.authDao.signUpUser(user);

		User actual = this.authDao.getUserByEmailAndPassword("jane_doe@gmail.com", "Jane!123");

		User expected = new User("Jane", "Doe", "jane_doe@gmail.com", "Jane!123", "5712561234", "Member");
		expected.setUserId(1);

		Assertions.assertEquals(expected, actual);

		this.entityManager.flush();
	}

	@Test
	@Transactional
	public void testSignUpUserAsMember_blankFirstName() {
		User user = new User(null, "Doe", "jane_doe@gmail.com", "Jane!123", "5712561234", "Member");

		Assertions.assertThrows(DataAccessException.class, () -> {
			this.authDao.signUpUser(user);
		});
	}

	@Test
	@Transactional
	public void testSignUpUserAsMember_blankLastName() {
		User user = new User("Jane", null, "jane_doe@gmail.com", "Jane!123", "5712561234", "Member");

		Assertions.assertThrows(DataAccessException.class, () -> {
			this.authDao.signUpUser(user);
		});
	}

	@Test
	@Transactional
	public void testSignUpUserAsMember_blankEmail() {
		User user = new User("Jane", "Doe", null, "Jane!123", "5712561234", "Member");

		Assertions.assertThrows(DataAccessException.class, () -> {
			this.authDao.signUpUser(user);
		});
	}

	@Test
	@Transactional
	public void testSignUpUserAsMember_blankPassword() {
		User user = new User("Jane", "Doe", "jane_doe@gmail.com", null, "5712561234", "Member");

		Assertions.assertThrows(DataAccessException.class, () -> {
			this.authDao.signUpUser(user);
		});
	}

	@Test
	@Transactional
	public void testSignUpUserAsMember_blankPhoneNumber() {
		User user = new User("Jane", "Doe", "jane_doe@gmail.com", "Jane!123", null, "Member");

		Assertions.assertThrows(DataAccessException.class, () -> {
			this.authDao.signUpUser(user);
		});
	}

	@Test
	@Transactional
	public void testSignUpUserAsMember_blankUserRole() {
		User user = new User("Jane", "Doe", "jane_doe@gmail.com", "Jane!123", "5712561234", null);

		Assertions.assertThrows(DataAccessException.class, () -> {
			this.authDao.signUpUser(user);
		});
	}

	@Test
	@Transactional
	public void testSignUpUserAsMember_allInPutBlank() {
		User user = new User(null, null, null, null, null, null);

		Assertions.assertThrows(DataAccessException.class, () -> {
			this.authDao.signUpUser(user);
		});
	}

	/*-	*****************
	 * 	getUserById Tests
	 * 	*****************
	 */
	@Test
	@Transactional
	public void testGetUserByUserId_positive() {
		User user = new User("Jane", "Doe", "jane_doe@gmail.com", "Jane!123", "5712561234", "Member");
		this.authDao.signUpUser(user);

		this.entityManager.flush();

		User actual = this.authDao.getUserByUserId(1);

		User expected = new User("Jane", "Doe", "jane_doe@gmail.com", "Jane!123", "5712561234", "Member");
		expected.setUserId(1);

		Assertions.assertEquals(expected, actual);

	}

	@Test
	@Transactional
	public void testGetUserByUserId_userIdBlank() {
		User user = new User("Jane", "Doe", "jane_doe@gmail.com", "Jane!123", "5712561234", "Member");

		this.entityManager.persist(user);

		this.entityManager.flush();

		User actual = this.authDao.getUserByUserId(0);

		User expected = new User("Jane", "Doe", "jane_doe@gmail.com", "Jane!123", "5712561234", "Member");
		expected.setUserId(1);

		Assertions.assertNotEquals(expected, actual);
	}

	@Test
	@Transactional
	public void testGetUserByUserId_userDoesNotExist() {
		User user = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		this.entityManager.persist(user);

		User expected = this.authDao.getUserByUserId(1);

		User actual = new User();

		Assertions.assertNotEquals(expected, actual);
	}

	/*- **********************************
	 * 	getUserByEmailAndPhoneNumber Tests
	 * 	**********************************
	 */
	@Test
	@Transactional
	public void testGetUserByEmailAndPhoneNumber_positive() {
		User user1 = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		this.entityManager.persist(user1);

		this.entityManager.flush();

		// ACT
		List<User> actual = this.authDao.getUserByEmailAndPhoneNumber("nedoe@yahoo.com", "2039008372");

		// ASSERT
		User user = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		user.setUserId(1);

		List<User> expected = new ArrayList<User>();
		expected.add(user);

		Assertions.assertEquals(expected, actual);

	}

	@Test
	@Transactional
	public void testGetUserByEmailAndPhoneNumber_incorrectEmail() {
		User user = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		this.entityManager.persist(user);

		this.entityManager.flush();

		List<User> actual = this.authDao.getUserByEmailAndPhoneNumber("123456@yahoo.com", "2039008372");

		// ASSERT
		User user1 = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		user1.setUserId(1);

		List<User> expected = new ArrayList<User>();
		expected.add(user);

		Assertions.assertEquals(expected, actual);

	}

	@Test
	@Transactional
	public void testGetUserByEmailAndPhoneNumber_incorrectPhoneNumber() {
		User user = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		this.entityManager.persist(user);

		this.entityManager.flush();

		List<User> actual = this.authDao.getUserByEmailAndPhoneNumber("nedoe@yahoo.com", "0123456789");

		// ASSERT
		User user1 = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		user1.setUserId(1);

		List<User> expected = new ArrayList<User>();
		expected.add(user);

		Assertions.assertEquals(expected, actual);

	}

	@Test
	@Transactional
	public void testGetUserByEmailAndPhoneNumber_incorrectEmailAndPhoneNumber() {
		User user = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		this.entityManager.persist(user);

		this.entityManager.flush();

		List<User> actual = this.authDao.getUserByEmailAndPhoneNumber("123456@yahoo.com", "0123456789");

		// ASSERT
		User user1 = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		user1.setUserId(1);

		List<User> expected = new ArrayList<User>();
		expected.add(user);

		Assertions.assertNotEquals(expected, actual);

	}

	@Test
	@Transactional
	public void testGetUserByEmailAndPhoneNumber_blankPhoneNumber() {
		User user = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		this.entityManager.persist(user);

		this.entityManager.flush();

		List<User> actual = this.authDao.getUserByEmailAndPhoneNumber("disIsMyPassword13", null);

		// ASSERT
		User user1 = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		user1.setUserId(1);

		List<User> expected = new ArrayList<User>();
		expected.add(user);

		Assertions.assertNotEquals(expected, actual);
	}

	@Test
	@Transactional
	public void testGetUserByEmailAndPhoneNumber_blankEmail() {
		User user = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		this.entityManager.persist(user);

		this.entityManager.flush();

		List<User> actual = this.authDao.getUserByEmailAndPhoneNumber(null, "2039008372");

		// ASSERT
		User user1 = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		user1.setUserId(1);

		List<User> expected = new ArrayList<User>();
		expected.add(user);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	@Transactional
	public void testGetUserByEmailAndPhoneNumber_blankEmailAndblankPhoneNumber() {
		User user = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		this.entityManager.persist(user);

		this.entityManager.flush();

		List<User> actual = this.authDao.getUserByEmailAndPhoneNumber(null, null);

		// ASSERT
		User user1 = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		user1.setUserId(1);

		List<User> expected = new ArrayList<User>();
		expected.add(user);

		Assertions.assertNotEquals(expected, actual);

	}

	@Test
	@Transactional
	public void testGetUserByEmailAndPhoneNumber_userDoesNotExist() {
		User user = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		this.entityManager.persist(user);

		User expected = this.authDao.getUserByEmailAndPassword("nedoe@yahoo.com", "2039008372");

		User actual = new User();

		Assertions.assertNotEquals(expected, actual);
	}

	/*- ********************
	 * 	getUserByEmail Tests
	 * 	********************
	 */
	@Test
	@Transactional
	public void testGetUserByEmail_positive() {
		User user = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		this.entityManager.persist(user);

		this.entityManager.flush();

		// ACT
		User actual = this.authDao.getUserByEmail("nedoe@yahoo.com");

		// ASSERT
		User expected = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		expected.setUserId(1);

		Assertions.assertEquals(expected, actual);

	}

	@Test
	@Transactional
	public void testGetUserByEmail_incorrectEmailInput() {
		User expected = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		this.entityManager.persist(expected);

		this.entityManager.flush();

		User actual = this.authDao.getUserByEmail("nodoe@yahoo.com");

		Assertions.assertNotEquals(expected, actual);

	}

	@Test
	@Transactional
	public void testGetUserByEmail_noInputInEmail() {
		User expected = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		this.entityManager.persist(expected);

		this.entityManager.flush();

		// ACT AND ASSERT
		User actual = this.authDao.getUserByEmail(null);

		Assertions.assertNotEquals(expected, actual);

	}

	@Test
	@Transactional
	public void testGetUserByEmail_userDoesNotExist() {
		User user = new User("Jane", "Doe", "nedoe@yahoo.com", "disIsMyPassword13", "2039008372", "Member");
		this.entityManager.persist(user);
		User expected = this.authDao.getUserByEmail("nedoe@yahoo.com");

		User actual = new User();

		Assertions.assertNotEquals(expected, actual);
	}

}
