package com.solitardj9.microiot.application.caManager.service.impl;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.IMap;
import com.solitardj9.microiot.application.caManager.model.CaCertificate;
import com.solitardj9.microiot.application.caManager.service.CaCertificateManager;
import com.solitardj9.microiot.application.caManager.service.dao.CaCertificateDao;
import com.solitardj9.microiot.application.caManager.service.dao.CaCertificateNativeQueryDao;
import com.solitardj9.microiot.application.caManager.service.dao.dto.CaCertificateDto;
import com.solitardj9.microiot.application.caManager.service.data.CaCertificateMapParamEnum;
import com.solitardj9.microiot.application.caManager.service.data.exception.ExceptionInMemoryManagerFailure;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.InMemoryInstane;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastDistributedObjectNameConflict;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastIMapBadRequest;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastIMapNotFound;
import com.solitardj9.microiot.systemInterface.imdgInterface.model.exception.ExceptionHazelcastServerAlreadyClosed;
import com.solitardj9.microiot.systemInterface.imdgInterface.service.InMemoryEventListener;
import com.solitardj9.microiot.systemInterface.imdgInterface.service.InMemoryServerManager;
import com.solitardj9.microiot.systemInterface.imdgInterface.service.impl.InMemoryServerManagerImpl;

@Service("caCertificateManager")
public class CaCertificateManagerImpl implements CaCertificateManager, InMemoryEventListener {

	private static final Logger logger = LoggerFactory.getLogger(InMemoryServerManagerImpl.class);
	
	@Autowired
	CaCertificateNativeQueryDao caCertificateNativeQueryDao;
	
	@Autowired
	CaCertificateDao caCertificateDao;
	
	@Autowired
	InMemoryServerManager inMemoryServerManager;
	
	@Value("${application.caManager.caCertificateManager.ca.certificate.issuer}")
	private String caCertificateIssuer;
	
	@Value("${application.caManager.caCertificateManager.ca.certificate.subject}}")
	private String caCertificateSubject;
	
	@Value("${application.caManager.caCertificateManager.ca.certificate.duration.years}")
	private Long caCertificateDurationYears;
	
	@Value("${application.caManager.caCertificateManager.ca.certificate.alarm.duration.days}")
	private Long caCertificateAlarmDurationDays;
	
	@Value("${application.caManager.caCertificateManager.ca.certificate.signatureAlgorithm}")
	private String caCertificateSignatureAlgorithm;
	
	@Value("${application.caManager.caCertificateManager.ca.privateKey.algorithm}")
	private String caPrivateKeyAlgorithm;
	
	@Value("${application.caManager.caCertificateManager.ca.privateKey.length}")
	private String caPrivateKeyLength;
	
	@Value("${application.caManager.caCertificateManager.ca.file.location}")
	private String caFileLocation;
	
	@Value("${application.caManager.caCertificateManager.ca.file.certificate.name}")
	private String caFileCertificateName;
	
	@Value("${application.caManager.caCertificateManager.ca.file.privateKey.name}")
	private String caFilePrivateKeyName;
	
	@Value("${application.caManager.caCertificateManager.memCluster.backupCount}")
	private Integer memClusterBackupCount;
	
	@Value("${application.caManager.caCertificateManager.memCluster.readBackup}")
	private Boolean memClusterReadBackup;
	
	@Value("${application.caManager.caCertificateManager.memCluster.lockTimeout.ms}")
	private Long memClusterLockTimeoutMs;
	
	private static final Long ONE_DAY_IN_MILISEC = 1000L * 60 * 60 * 24;
	
	private String osName = System.getProperty("os.name").toLowerCase();
	
	private InMemoryInstane inMemoryInstane;
	
	private Boolean isInitialized = false;
	
	private static final Integer INDEX_OF_CA = 1; 
	
	@PostConstruct
	public void init() {
		//
		try	{
			createTable();
			createMap();
			
			CaCertificateDto caCertificateDtoFromDB = getCaCertificateFromDB();
			CaCertificateDto caCertificateDtoFromMem = getCaCertificateFromMem();
			
			
			
		} catch(Exception e) {
			logger.error("[CaCertificateManager].createMap : error = " + e);
			return;
		}
        
//        
//        // 2) Site CA Certificate가 DB에 있으면 
//        if (caCertificateDtoFromDB != null) {
//            // 2-1) In-Memory 조회
//            caCertificateDtoFromIMDG = getCaCertificateFromIMDG();
//            
//            // 2-2) In-Memory에 없을 경우엔 DB 조회 결과를 In-Memory에 저장
//            if (caCertificateDtoFromIMDG == null) {
//                setCaCertificateToIMDG(caCertificateDtoFromDB);
//            }
//
//            // 2-3) File이 없으면 File 저장
//            try {
//                if (!findFile()) {
//                    saveFile(caCertificateDtoFromDB.getCaCertificatePem(), caCertificateDtoFromDB.getPrivateKeyPem());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            isInitialized = true;
//            return;
//        } 
//
//        // 3) Site CA Certificate가 DB에 없으면 
//        //    - Site CA 생성 후 DB & Memory 저장/공유 & 파일 저장
//        createNewCaCertificate();
        
        isInitialized = true;
        
        
        
        
	}
	
	@Override
	public Boolean isInitialized() {
		return isInitialized;
	}
	
	private void createTable() {
		//
		caCertificateNativeQueryDao.createCaCertificateTable();
	}
	
	private void createMap() throws ExceptionInMemoryManagerFailure {
		//
		inMemoryInstane = new InMemoryInstane(CaCertificateMapParamEnum.CA_CERTIFICATE_MAP.getParam(), 
												   memClusterBackupCount, 
												   memClusterReadBackup, 
												   CaCertificateMapParamEnum.CA_CERTIFICATE_MAP_LOCK.getParam(), 
												   this);
		try {
			inMemoryServerManager.createMap(inMemoryInstane);
		} catch (ExceptionHazelcastServerAlreadyClosed | ExceptionHazelcastDistributedObjectNameConflict | ExceptionHazelcastIMapBadRequest e) {
			logger.error("[CaCertificateManager].createMap : error = " + e);
			throw new ExceptionInMemoryManagerFailure();
		}
	}
	
	private CaCertificateDto getCaCertificateFromDB() {
		//
		CaCertificateDto caCertificateDto = null;
		
		Optional<CaCertificateDto> result = caCertificateDao.findById(INDEX_OF_CA);
		if (result.isPresent()) {
			caCertificateDto = result.get();
		}
		return caCertificateDto;
	}

	private CaCertificateDto getCaCertificateFromMem() throws ExceptionInMemoryManagerFailure {
		//
		try {
			CaCertificateDto caCertificateDto = null;
			
			Object result = inMemoryServerManager.getMap(CaCertificateMapParamEnum.CA_CERTIFICATE_MAP.getParam()).get(CaCertificateMapParamEnum.CA_CERTIFICATE_MAP_KEY.getParam());
			if (result != null) {
				caCertificateDto = (CaCertificateDto)result;
			}
			return caCertificateDto;
		} catch (ExceptionHazelcastServerAlreadyClosed | ExceptionHazelcastIMapNotFound e) {
			logger.error("[CaCertificateManager].getCaCertificateFromMem : error = " + e);
			throw new ExceptionInMemoryManagerFailure();
		}
	}
	
	@Override
	public CaCertificate getCa() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void entryAdded(EntryEvent<Object, Object> event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void entryRemoved(EntryEvent<Object, Object> event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void entryUpdated(EntryEvent<Object, Object> event) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
//    private void createNewCaCertificate() {
//        //
//        CaCertificate caCertificate = null;
//        try {
//            caCertificate = createCaCertificate();
//            CaCertificateDto caCertificateDto = changeCaCertificateToCaCertificateDto(caCertificate);
//            
//            if (caCertificateDto != null) {
//                // 3-1) DB 저장
//                setCaCertificateToDB(caCertificateDto);
//                
//                // 3-2) In-Memory 저장
//                setCaCertificateFromIMDGWithLock(caCertificateDto);
//                
//                // 3-3) File 저장
//                saveFile(caCertificateDto.getCaCertificatePem(), caCertificateDto.getPrivateKeyPem());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    
//    private void createNewCaCertificateByExternalRequst(String caCertificatePem, String publicKeyPem, String privateKeyPem) {
//        //
//        CaCertificate caCertificate = null;
//        try {
//            caCertificate = createCaCertificateByExternalRequest(caCertificatePem, publicKeyPem, privateKeyPem);
//            CaCertificateDto caCertificateDto = changeCaCertificateToCaCertificateDto(caCertificate);
//            
//            if (caCertificateDto != null) {
//                // 3-1) DB 저장
//                setCaCertificateToDB(caCertificateDto);
//                
//                // 3-2) In-Memory 저장
//                setCaCertificateFromIMDGWithLock(caCertificateDto);
//                
//                // 3-3) File 저장
//                saveFile(caCertificateDto.getCaCertificatePem(), caCertificateDto.getPrivateKeyPem());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    
//    @Override
//    public Boolean isInitialized() {
//        return isInitialized;       
//    }
//    
//    private InMemoryInstane createInMemoryInstane() {
//        //
//        inMemoryInstane = new InMemoryInstane();
//        
//        inMemoryInstane.setName(CaCertificateMapEnum.CA_CERT_MAP.toString());
//        inMemoryInstane.setBackupCount(backupCountMapState);
//        inMemoryInstane.setReadBackupData(readBackupData);
//        inMemoryInstane.setLockName(CaCertificateMapEnum.CA_CERT_MAP_LOCK.toString());
//        inMemoryInstane.setEventListener(this);
//        
//        return inMemoryInstane;
//    }
//    
//    private CaCertificate createCaCertificate() throws Exception {
//        //
//        CaCertificate caCertificate = null;
//        
//        // 1)  keyPair 생성
//        KeyPair keyPair = CertificateUtil.generateKeyPair(algorithm, KEY_LENGTH);       //Key spec 설정
//        
//        // 2) CA Certificate 생성을 위한 요청 객체 생성 및 유효성 확인 
//        CertificateInfo certificateInfo = new CertificateInfo(duration, new X500Name(CN + issuer), new X500Name(CN + subject), BigInteger.valueOf((int)new SecureRandom().nextInt()), KeyUsage.keyCertSign, true);
//        if (!checkCertificateInfo(certificateInfo)) {
//            logger.error("[CaCertManager].checkCertificateInfo : certificateInfo is not valid.");
//            return null;
//        }
//        
//        // 3) 현재 시각 기준 유효기간 생성
//        LocalDate now = LocalDate.now();
//        Date createdDate = Date.valueOf(now);                                                           // start of validity
//        Date expiredDate = Date.valueOf(now.plusYears(certificateInfo.getValidDuration()));             // end of validity
//        
//        // 4) 인증서 생성
//        X509Certificate x509Certificate = CertificateUtil.generateX509Certificate(certificateInfo, keyPair, createdDate, expiredDate, signatureAlgorithm);
//        
//        // 5) make CA Certificate
//        caCertificate = new CaCertificate(keyPair.getPrivate(), x509Certificate);
//        
//        return caCertificate;
//    }
//    
//    private CaCertificate createCaCertificateByExternalRequest(String caCertificatePem, String publicKeyPem, String privateKeyPem) throws Exception {
//        //
//        CaCertificate caCertificate = null;
//        
//        // 1)  keyPair 변환
//        PrivateKey privateKey = CertificateUtil.readPrivateKey(privateKeyPem, algorithm);
//        PublicKey publicKey = CertificateUtil.readPublicKey(publicKeyPem, algorithm);
//        KeyPair keyPair = new KeyPair(publicKey, privateKey);
//        
//        // 2) 인증서 변환
//        X509Certificate x509Certificate = CertificateUtil.readX509Certificate(caCertificatePem);
//        
//        // 5) make CA Certificate
//        caCertificate = new CaCertificate(keyPair.getPrivate(), x509Certificate);
//        
//        return caCertificate;
//    }
//    
//    private Boolean checkCertificateInfo(CertificateInfo certificateInfo) {
//        //
//        if (certificateInfo.getIssuerName() == null) {
//            logger.error("[CaCertManager].checkCertificateInfo : Issuer's information is nothing!");
//            return false;
//        }
//        
//        if (certificateInfo.getSubjectName() == null) {
//            logger.error("[CaCertManager].checkCertificateInfo : Subject's information is nothing!");
//            return false;
//        }
//        
//        if (certificateInfo.getKeyUsage() != KeyUsage.keyCertSign) {
//            logger.error("[CaCertManager].checkCertificateInfo : keyUsage is wrong!");
//            certificateInfo.setKeyUsage(KeyUsage.keyCertSign);
//            return false;
//        }
//        
//        if (!certificateInfo.getBasicConstratints()) {
//            logger.error("[CaCertManager].checkCertificateInfo : BasicConstratint must be true!");
//            certificateInfo.setBasicConstratints(true);
//            return false;
//        }
//        
//        if (certificateInfo.getValidDuration() < 0 ) {
//            logger.error("[CaCertManager].checkCertificateInfo : ValidDuration is wrong!");
//            return false;
//        }
//        
//        return true;
//    }
//    
//    
//    
//    
//    private void setCaCertificateToDB(CaCertificateDto caCertificateDto) {
//        //
//        Map<String, Object> params = new HashMap<>();
//        params.put(CaCertificateEnum.SITE_CA_PV_KEY.getColName(), caCertificateDto.getPrivateKeyPem());
//        params.put(CaCertificateEnum.SITE_CA_CERT.getColName(), caCertificateDto.getCaCertificatePem());
//        
//        CaCertificateDto existCaCertificateDto = getCaCertificateFromDB();
//        if (existCaCertificateDto == null) {    // insert
//            caCertDao.insertCaCert(params);
//        }
//        else {  // update
//            params.put(CaCertificateEnum.SEQ.getColName(), 1);
//            caCertDao.updateCaCert(params);
//        }
//    }
//    

//    
//    private void setCaCertificateToIMDG(CaCertificateDto caCertificateDto) {
//        //
//        if (caCertificateDto != null) {
//            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//            caCertificateDto.setTimestamp(timestamp);
//            
//            IMap<Object, Object> map = inMemoryManager.getMap(CaCertificateMapEnum.CA_CERT_MAP.toString());
//            if (map != null) {
//                map.put(CaCertificateMapEnum.CA_CERT_MAP_KEY.toString(), caCertificateDto);
//            }
//        }
//    }
//    
//    public void setCaCertificateFromIMDGWithLock(CaCertificateDto caCertificateDto) {
//        // 1) lock 
//        lockCaCertificateMapInIMDG();
//
//        // 2) update
//        setCaCertificateToIMDG(caCertificateDto);
//        
//        // 3) unlock
//        unlockCaCertificateMapInIMDG();
//    }
//    
//    private void lockCaCertificateMapInIMDG() {
//        //
//        String lock = CaCertificateMapEnum.CA_CERT_MAP_LOCK.toString();
//        
//        IMap<Object, Object> map = inMemoryManager.getMap(CaCertificateMapEnum.CA_CERT_MAP.toString());
//        if (map != null) {
//            map.lock(lock, LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
//        }
//    }
//    
//    private void unlockCaCertificateMapInIMDG() {
//        //
//        String lock = CaCertificateMapEnum.CA_CERT_MAP_LOCK.toString();
//        IMap<Object, Object> map = inMemoryManager.getMap(CaCertificateMapEnum.CA_CERT_MAP.toString());
//        if (map != null) {
//            if (map.isLocked(lock)) {
//                map.unlock(lock);
//            }
//        }
//    }
//    
//    private Boolean findFile() throws Exception {
//        // File 변환
//        FileInputStream fileInputStream = null;
//        FileInputStream fileInputKeyStream = null;
//        try {
//            fileInputStream = new FileInputStream(dir + SITE_CA_CERT_FILE_NAME);
//            fileInputKeyStream = new FileInputStream(dir + SITE_CA_PV_KEY_FILE_NAME);
//        
//            
//        } catch (IOException e) {
//            logger.error("[CACertManager] findFile : " + e.toString());
//            return false;
//        } finally {
//            if (fileInputStream != null) {
//                fileInputStream.close();
//            }
//            if (fileInputKeyStream != null) {
//                fileInputKeyStream.close();
//            }
//        }
//        return true;
//    }
//    
//    private void saveFile(CaCertificate caCertificate) {
//        //
//        if (caCertificate == null) {
//            logger.error("[CaCertManager].saveFile : caCertificate is null.");
//            return;
//        }
//        
//        // 1) X509Certificate --> pem(String) 변환
//        // 2) KeyPair(Private Key) --> pem(String ) 변환
//        String pemCertificate = null;
//        String pemPrivateKey = null;
//        try {
//            pemCertificate = CertificateUtil.makeX509CertificateAsPem(caCertificate.getCaCertificate());
//            pemPrivateKey = CertificateUtil.privateKeyAsPem(caCertificate.getPrivateKey());
//        } catch (CertificateEncodingException | IOException e) {
//            logger.error("[CaCertManager].saveFile : " + e.toString());
//            return;
//        }
//        
//        // 3) File 저장
//        saveFile(pemCertificate, pemPrivateKey);
//    }
//    
//    private void saveFile(String pemCertificate, String pemPrivateKey) {
//        //
//        FileOutputStream fileOutputStream = null;
//        try {
//            //File file = new File(dir + SITE_CA_CERT_FILE_NAME);
//            //file.createNewFile();
//            //FileWriter fw = new FileWriter(file.getAbsoluteFile());
//            //BufferedWriter bw = new BufferedWriter(fw);
//            
//            fileOutputStream = new FileOutputStream(dir + SITE_CA_CERT_FILE_NAME);
//            fileOutputStream.write(pemCertificate.getBytes(Charset.forName("UTF-8")));
//            fileOutputStream.flush();
//            fileOutputStream.close();
//        } catch (IOException e) {
//            logger.error("[CaCertManager].saveFile : " + e.toString());
//            return;
//        } finally {
//            if (fileOutputStream != null) {
//                try {
//                    fileOutputStream.close();
//                } catch (IOException e) {
//                    logger.error("[CaCertManager].saveFile : " + e.toString());
//                    return;
//                }
//            }
//        }
//        
//        try {
//            fileOutputStream = new FileOutputStream(dir + SITE_CA_PV_KEY_FILE_NAME);
//            fileOutputStream.write(pemPrivateKey.getBytes(Charset.forName("UTF-8")));
//            fileOutputStream.flush();
//            fileOutputStream.close();
//        } catch (IOException e) {
//            logger.error("[CaCertManager].saveFile : " + e.toString());
//            return;
//        } finally {
//            if (fileOutputStream != null) {
//                try {
//                    fileOutputStream.close();
//                } catch (IOException e) {
//                    logger.error("[CaCertManager].saveFile : " + e.toString());
//                    return;
//                }
//            }
//        }
//    }
//    
//    private CaCertificate changeCaCertificateDtoToCaCertificate(CaCertificateDto caCertificateDto) {
//        //
//        if (caCertificateDto != null) {
//            try {
//                PrivateKey pirvateKey = CertificateUtil.readPrivateKey(caCertificateDto.getPrivateKeyPem(), algorithm);
//                X509Certificate x509Certificate = CertificateUtil.readX509Certificate(caCertificateDto.getCaCertificatePem());
//                
//                CaCertificate caCertificate = new CaCertificate(pirvateKey, x509Certificate);
//                return caCertificate;
//            } catch (Exception e) {
//                logger.error("[CaCertManager].changeCaCertificateDtoToCaCertificate : " + e.toString());
//                return null;
//            }
//        }
//        return null;
//    }
//
//    private CaCertificateDto changeCaCertificateToCaCertificateDto(CaCertificate caCertificate) {
//        //
//        if (caCertificate != null) {
//            try {
//                String strPirvateKey = CertificateUtil.privateKeyAsPem(caCertificate.getPrivateKey());
//                String strX509Certificate = CertificateUtil.makeX509CertificateAsPem(caCertificate.getCaCertificate());
//                
//                CaCertificateDto caCertificateDto = new CaCertificateDto(strPirvateKey, strX509Certificate);
//                return caCertificateDto;
//            } catch (Exception e) {
//                logger.error("[CaCertManager].changeCaCertificateToCaCertificateDto : " + e.toString());
//                return null;
//            }
//        }
//        return null;
//    }
//    
//    @Override
//    public CaCertificate getCaCertificate() {
//        //
//        return changeCaCertificateDtoToCaCertificate(getCaCertificateFromDB());
//    }
//    
//    private void restartRabbitMQService() {
//        //
//        String os = getOS();
//        
//        if (os.equals("win")) {
//            restartRabbitMQServiceOnWindows();
//        }
//        else if (os.equals("unix")) {
//            restartRabbitMQServiceOnUnix();
//        }
//        else {
//            logger.error("[CaCertManager].restartRabbitMQService : not supported O/S.");
//        }
//    }
//    
//    private void restartRabbitMQServiceOnWindows() {
//        //
//        String SERVICE_NAME = "RabbitMQ";
//        
//        String[] start_script = {"cmd.exe", "/c", "sc", "start", SERVICE_NAME};
//        String[] stop_script = {"cmd.exe", "/c", "sc", "stop", SERVICE_NAME};
//        String[] check_script = {"cmd.exe", "/c", "sc", "query", SERVICE_NAME, "|", "find", "/C", "\"RUNNING\""};
//        
//        try {
//            // 1) 동작상태 확인
//            Process alive_process = Runtime.getRuntime().exec(check_script);
//            int exitVal = alive_process.waitFor();  // 0: alive, 1: stop
//            
//            // 2) 현재 동작 중이면 중지 후 재시작
//            if (exitVal == 0) {
//                Process stop_process = Runtime.getRuntime().exec(stop_script);
//                stop_process .waitFor(10L, TimeUnit.SECONDS);
//                exitVal = stop_process.waitFor();
//            }
//            
//            Integer count_limit = 30;
//            Integer count = 0;
//            while(true) {
//                //
//                if (count > count_limit) {
//                    break;
//                }
//                
//                alive_process = Runtime.getRuntime().exec(check_script);
//                exitVal = alive_process.waitFor();  // 0: alive, 1: stop
//                if (exitVal != 0) {
//                    break;
//                }
//                Thread.sleep(1000); // 30sec
//                count++;
//            }
//            
//            // 3) 재시작
//            Process start_process = Runtime.getRuntime().exec(start_script);
//            start_process .waitFor(10L, TimeUnit.SECONDS);
//            exitVal = start_process.waitFor();
//            
//            alive_process = Runtime.getRuntime().exec(check_script);
//            exitVal = alive_process.waitFor();  // 0: alive, 1: stop
//            if (exitVal == 0) {
//                logger.info("[CaCertManager].restartRabbitMQServiceOnWindows : rabbitMQ service is started");
//            }
//            else {
//                logger.error("[CaCertManager].restartRabbitMQServiceOnWindows : rabbitMQ service is not started");
//            }
//            
//        } catch (IOException | InterruptedException e) {
//            logger.error("[CaCertManager].restartRabbitMQServiceOnWindows : " + e.toString());
//        }
//    }
//    
//    private void restartRabbitMQServiceOnUnix() {
//        //
//        String SERVICE_NAME = "rabbitmq-server";
//                
//        String[] start_script = {"sh", "-c", "service " + SERVICE_NAME + " start"};
//        String[] stop_script = {"sh", "-c", "service " + SERVICE_NAME + " stop"};
//        String[] check_script = {"sh", "-c", "service " + SERVICE_NAME + " status"};
//        
//        try {
//            // 1) 동작상태 확인
//            Process alive_process = Runtime.getRuntime().exec(check_script);
//            int exitVal = alive_process.waitFor();  // 0: alive, 1: stop
//            
//            // 2) 현재 동작 중이면 중지 후 재시작
//            if (exitVal == 0) {
//                try {
//                    Process stop_process = Runtime.getRuntime().exec(stop_script);
//                    stop_process.waitFor(10L, TimeUnit.SECONDS);
//                    exitVal = stop_process.waitFor();
//                    System.out.println("stop exitVal : " + exitVal);
//                } catch (InterruptedException e) {
//                    System.out.println(e.toString());
//                }
//                
//            }
//            
//            Integer count_limit = 30;
//            Integer count = 0;
//            while(true) {
//                //
//                if (count > count_limit) {
//                    break;
//                }
//                
//                alive_process = Runtime.getRuntime().exec(check_script);
//                exitVal = alive_process.waitFor();  // 0: alive, 3: stop
//                System.out.println("alive exitVal : " + exitVal);
//                if (exitVal != 0) {
//                    break;
//                }
//                Thread.sleep(1000); // 30sec
//                count++;
//            }
//            
//            // 3) 재시작
//            Process start_process = Runtime.getRuntime().exec(start_script);
//            start_process .waitFor(10L, TimeUnit.SECONDS);
//            exitVal = start_process.waitFor();
//            
//            alive_process = Runtime.getRuntime().exec(check_script);
//            exitVal = alive_process.waitFor();  // 0: alive, 3: stop
//            System.out.println("alive exitVal : " + exitVal);
//            if (exitVal == 0) {
//                logger.info("[CaCertManager].restartRabbitMQServiceOnUnix : rabbitMQ service is started");
//            }
//            else {
//                logger.error("[CaCertManager].restartRabbitMQServiceOnUnix : rabbitMQ service is not started");
//            }
//            
//        } catch (IOException | InterruptedException e) {
//            logger.error("[CaCertManager].restartRabbitMQServiceOnUnix : " + e.toString());
//        }
//    }
//    
//    private String getOS() {
//        //
//        if (isWindows()) {
//            return "win";
//        } else if (isUnix()) {
//            return "unix";
//        } else {
//            return null;
//        }
//    }
//  
//    private Boolean isWindows() {
//        return (OS.indexOf("win") >= 0);
//    }
//  
//    private Boolean isUnix() {
//        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
//    }
//    
//    /**
//     * 
//     * Method Name : checkCaCertificateValidation
//     * Method Desc : 인증서 유효기간 확인
//     * - 매일 12시에 체크한다.
//     * - 인증서 만료 전 n일 전에 Log 를 남긴다.
//     * - 인증서 시작, 종료 날짜 유효한지 체크한다.
//     */
//    @Scheduled(cron = "0 0 12 * * *") 
//    private void checkCaCertificateValidation() {
//        // 
//        CaCertificate caCertificate = getCaCertificate();
//        if (caCertificate == null) {
//            logger.error("[CaCertManager].checkCaCertificateValidation : certificate is null.");
//            return;
//        }
//        
//        X509Certificate x509Certificate = caCertificate.getCaCertificate();
//        
//        Date vaildDay = new Date(System.currentTimeMillis() + ONE_DAY_IN_MILISEC * warningDays);
//
//        try {
//            x509Certificate.checkValidity(vaildDay);
//        } catch (CertificateExpiredException e) {
//            logger.info("[CaCertManager].checkCaCertificateValidation : Site CA is (or will be ) expired. (" + e.toString() + ")");
//            logger.info("[CaCertManager].checkCaCertificateValidation : New Site CA will be updated automatically.");
//            createNewCaCertificate(); 
//        } catch (CertificateNotYetValidException e) {
//            logger.info("[CaCertManager].checkCaCertificateValidation : Site CA has invalid start date. (" + e.toString() + ")");
//        }
//    }
//
//    @Override
//    public void updateCaCertificate() {
//        //
//        createNewCaCertificate();
//    }
//
//    @Override
//    public void updateCaCertificate(String caCertificatePem, String publicKeyPem, String privateKeyPem) {
//        //
//        createNewCaCertificateByExternalRequst(caCertificatePem, publicKeyPem, privateKeyPem);
//    }
//
//    @Override
//    public void entryAdded(EntryEvent<Object, Object> event) {
//        //
//        logger.info("[CaCertManager].entryAdded : certificateInfo is added.");
//        
//        CaCertificateDto caCertificateDtoFromIMDG = (CaCertificateDto)event.getValue();
//        
//        saveFile(caCertificateDtoFromIMDG.getCaCertificatePem(), caCertificateDtoFromIMDG.getPrivateKeyPem());
//        
//        //clientCertManager.createClientCertificate();
//        
//        //restartRabbitMQService();
//    }
//
//    @Override
//    public void entryRemoved(EntryEvent<Object, Object> event) {
//        //
//        logger.error("[CaCertManager].entryRemoved : certificateInfo is deleted.");
//        
//        //CaCertificateDto caCertificateDtoFromIMDG = (CaCertificateDto)event.getOldValue();
//    }
//
//    @Override
//    public void entryUpdated(EntryEvent<Object, Object> event) {
//        //
//        logger.info("[CaCertManager].entryUpdated : certificateInfo is updated.");
//        
//        //CaCertificateDto prevCaCertificateDtoFromIMDG = (CaCertificateDto)event.getOldValue();
//        CaCertificateDto caCertificateDtoFromIMDG = (CaCertificateDto)event.getValue();
//        
//        saveFile(caCertificateDtoFromIMDG.getCaCertificatePem(), caCertificateDtoFromIMDG.getPrivateKeyPem());
//        
//        clientCertManager.createClientCertificate();
//        
//        restartRabbitMQService();
//    }
}
