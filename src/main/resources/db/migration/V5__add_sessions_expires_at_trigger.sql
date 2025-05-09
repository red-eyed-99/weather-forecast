CREATE TRIGGER check_expires_at_in_future BEFORE INSERT ON sessions
FOR EACH ROW
BEGIN
    IF NEW.expires_at <= NOW() THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'expires_at must be in the future';
    END IF;
END;