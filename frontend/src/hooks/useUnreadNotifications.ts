"use client";

import { getUnreadCount } from "@/services/notification.service";
import { useEffect, useState } from "react";

export function useUnreadNotifications() {
  const [count, setCount] = useState(0);

  useEffect(() => {
    async function load() {
      try {
        const data = await getUnreadCount();
        setCount(data);
      } catch (e) {
        console.error(e);
      }
    }

    load();

    const interval = setInterval(load, 30000);

    return () => clearInterval(interval);
  }, []);

  return count;
}