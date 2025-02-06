#ifndef THREAD_H
#define THREAD_H
 
#include "ns3/core-config.h"
#include "ns3/callback.h"
// #ifdef HAVE_PTHREAD_H
#include <pthread.h>
// #endif
 
namespace ns3 { 
 
class SystemThread : public SimpleRefCount<SystemThread>
{
public:
 
// #ifdef HAVE_PTHREAD_H
 
   typedef pthread_t ThreadId;
// #endif
 
   SystemThread(Callback<void> callback);
 
   ~SystemThread();
 
   void Start (void);
 
   void Join (void);
   static ThreadId Self(void);
 
   static bool Equals(ThreadId id);
 
private:
// #ifdef HAVE_PTHREAD_H
 
   static void *DoRun (void *arg);
 
   Callback<void> m_callback;  
   pthread_t m_thread;  
// #endif 
};
 
} // namespace ns3
 
#endif