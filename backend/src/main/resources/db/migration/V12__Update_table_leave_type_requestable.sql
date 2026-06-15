-- Add column requestable in leave_type
ALTER TABLE business.leave_type
    ADD COLUMN requestable BOOLEAN NOT NULL DEFAULT FALSE;