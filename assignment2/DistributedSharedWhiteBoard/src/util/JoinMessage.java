package util;

public enum JoinMessage {
	SUCCESS, // successful join
	REFUSED, // Refused by the manager
	NO_MANAGER, // The manager has not created the white board yet
	MANAGER_EXIST, // someone joins as a manager while the manager has already existed
	INVALID_NAME, // The user name is duplicated
}
